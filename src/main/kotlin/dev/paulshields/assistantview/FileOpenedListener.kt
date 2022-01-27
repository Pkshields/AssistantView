package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import dev.paulshields.assistantview.common.Dispatcher
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

        dispatcher.runOnBackgroundThread {
            val openedFile = getFileFromFileEvent(event)
            val counterpartFile = openedFile?.let { getCounterpartFile(it) }

            counterpartFile?.let { openFileInAssistantView(it) }

            logInfo { "File opened: ${openedFile?.fileName}; counterpart file: ${counterpartFile?.fileName}" }
        }
    }

    private fun getFileFromFileEvent(event: FileEditorManagerEvent) =
        runWithReadPermission { fileManagerService.getFileFromVirtualFile(event.newFile, event.manager.project) }

    private fun getCounterpartFile(file: AssistantViewFile) =
        runWithReadPermission { fileAssistantService.getCounterpartFile(file) }

    private fun openFileInAssistantView(counterpartFile: AssistantViewFile) =
        runOnUiThread { assistantViewService.openFile(counterpartFile) }
}
