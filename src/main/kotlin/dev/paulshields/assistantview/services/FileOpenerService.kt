package dev.paulshields.assistantview.services

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.paulshields.assistantview.common.Dispatcher
import dev.paulshields.assistantview.extensions.isDumb
import dev.paulshields.assistantview.extensions.runOnUiThread
import dev.paulshields.assistantview.extensions.runWithReadPermission
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.lok.logDebug
import dev.paulshields.lok.logInfo

class FileOpenerService(
    private val counterpartFileService: CounterpartFileService,
    private val fileManagerService: FileManagerService,
    private val assistantViewService: AssistantViewService,
    private val dispatcher: Dispatcher) {

    fun openCounterpartForFile(rawFile: VirtualFile, project: Project) {
        if (project.isDumb) {
            openCounterpartFileWhenProjectExitsDumbMode(project)
            return
        }

        findAndOpenCounterpartFile(rawFile, project)
    }

    fun openCounterpartForTabInFocus(project: Project) {
        getRawFileFromCurrentTabInFocus(project)?.let {
            openCounterpartForFile(it, project)
        } ?: logInfo { "New Assistant View is available but there are no opened tabs in project ${project.name}. Dropping event." }
    }

    private fun openCounterpartFileWhenProjectExitsDumbMode(project: Project) {
        DumbService.getInstance(project).runWhenSmart {
            getRawFileFromCurrentTabInFocus(project)?.let {
                findAndOpenCounterpartFile(it, project)
            } ?: logInfo { "Dumb mode has finished but no file is open for project ${project.name}. Ignoring." }
        }
    }

    private fun getRawFileFromCurrentTabInFocus(project: Project) = FileEditorManager.getInstance(project).selectedEditor?.file

    private fun findAndOpenCounterpartFile(rawFile: VirtualFile, project: Project) = dispatcher.runOnBackgroundThread {
        if (!assistantViewService.assistantViewExistsForProject(project)) {
            logDebug { "File opened event received but no Assistant View window is open for this project. Ignoring..." }
            return@runOnBackgroundThread
        }

        val openedFile = getAssistantViewFile(rawFile, project)
        val counterpartFile = openedFile?.let { getCounterpartFile(it) }

        counterpartFile?.let { openFileInAssistantView(it) }

        logInfo { "File opened: ${openedFile?.fileName}; counterpart file: ${counterpartFile?.fileName}" }
    }

    private fun getAssistantViewFile(rawFile: VirtualFile, project: Project) =
        runWithReadPermission { fileManagerService.getFileFromVirtualFile(rawFile, project) }

    private fun getCounterpartFile(file: AssistantViewFile) =
        runWithReadPermission { counterpartFileService.findCounterpartFile(file) }

    private fun openFileInAssistantView(counterpartFile: AssistantViewFile) =
        runOnUiThread { assistantViewService.openFile(counterpartFile) }
}
