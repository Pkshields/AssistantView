package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.paulshields.assistantview.common.Dispatcher
import dev.paulshields.assistantview.extensions.isDumb
import dev.paulshields.assistantview.extensions.runOnUiThread
import dev.paulshields.assistantview.extensions.runWithReadPermission
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.lok.logInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FileOpenedListener : FileEditorManagerListener, KoinComponent {
    private val fileAssistantService: FileAssistantService by inject()
    private val fileManagerService: FileManagerService by inject()
    private val assistantViewService: AssistantViewService by inject()
    private val dispatcher: Dispatcher by inject()

    override fun selectionChanged(event: FileEditorManagerEvent) {
        if (event.newFile == null) return

        if (event.manager.project.isDumb) {
            openCounterpartFileWhenProjectExitsDumbMode(event.manager)
            return
        }

        findAndOpenCounterpartFile(event.newFile, event.manager.project)
    }

    private fun openCounterpartFileWhenProjectExitsDumbMode(fileEditorManager: FileEditorManager) =
        DumbService.getInstance(fileEditorManager.project).runWhenSmart {
            fileEditorManager.selectedEditor?.file?.let {
                findAndOpenCounterpartFile(it, fileEditorManager.project)
            } ?: logInfo { "Dumb mode has finished but no file is open for project ${fileEditorManager.project.name}. Ignoring." }
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
        runWithReadPermission { fileAssistantService.getCounterpartFile(file) }

    private fun openFileInAssistantView(counterpartFile: AssistantViewFile) =
        runOnUiThread { assistantViewService.openFile(counterpartFile) }
}
