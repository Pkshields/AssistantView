package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import org.koin.core.KoinComponent
import org.koin.core.inject

class OpenFileAssistantListener : FileEditorManagerListener, KoinComponent {

    private val fileAssistantService: FileAssistantService by inject()
    private val fileManagerService: FileManagerService by inject()

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val currentlyOpenedFile = event.newFile ?: return
        val project = event.manager.project

        fileManagerService.getFileFromProject(currentlyOpenedFile, project)?.let {
            val counterpartFile = fileAssistantService.getCounterpartFile(it, project)
        }
    }
}
