package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import dev.paulshields.lok.logInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FileOpenedListener : FileEditorManagerListener, KoinComponent {
    private val fileAssistantService: FileAssistantService by inject()
    private val fileManagerService: FileManagerService by inject()

    override fun selectionChanged(event: FileEditorManagerEvent) {
        if (event.newFile == null) return

        val file = fileManagerService.getFileFromVirtualFile(event.newFile, event.manager.project)
        val counterpartFile = file?.let { fileAssistantService.getCounterpartFile(it) }

        logInfo { "File opened: ${file?.name}; counterpart file: ${counterpartFile?.name}" }
    }
}
