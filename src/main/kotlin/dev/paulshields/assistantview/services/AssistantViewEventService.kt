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
import dev.paulshields.lok.logInfo

class AssistantViewEventService(
    private val counterpartFileService: CounterpartFileService,
    private val fileManagerService: FileManagerService,
    private val assistantViewService: AssistantViewService,
    private val dispatcher: Dispatcher) {

    fun handleFileOpenedEvent(rawFile: VirtualFile, project: Project) {
        if (project.isDumb) {
            openCounterpartFileWhenProjectExitsDumbMode(project)
            return
        }

        findAndOpenCounterpartFile(rawFile, project)
    }

    private fun openCounterpartFileWhenProjectExitsDumbMode(project: Project) {
        val dumbService = DumbService.getInstance(project)
        val fileEditorManager = FileEditorManager.getInstance(project)

        dumbService.runWhenSmart {
            fileEditorManager.selectedEditor?.file?.let {
                findAndOpenCounterpartFile(it, project)
            } ?: logInfo { "Dumb mode has finished but no file is open for project ${fileEditorManager.project.name}. Ignoring." }
        }
    }

    private fun findAndOpenCounterpartFile(rawFile: VirtualFile, project: Project) = dispatcher.runOnBackgroundThread {
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
