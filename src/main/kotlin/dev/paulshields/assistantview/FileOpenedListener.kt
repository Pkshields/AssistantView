package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import dev.paulshields.lok.logInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FileOpenedListener : FileEditorManagerListener, KoinComponent {
    private val fileAssistantService: FileAssistantService by inject()
    private val fileManagerService: FileManagerService by inject()
    private val assistantViewService: AssistantViewService by inject()

    override fun selectionChanged(event: FileEditorManagerEvent) {
        if (event.newFile == null) return

        val openedFile = fileManagerService.getFileFromVirtualFile(event.newFile, event.manager.project)
        val counterpartFile = openedFile?.let { fileAssistantService.getCounterpartFile(it) }

        counterpartFile?.let { assistantViewService.openFile(it) }

        logInfo { "File opened: ${openedFile?.fileName}; counterpart file: ${counterpartFile?.fileName}" }
    }
}
