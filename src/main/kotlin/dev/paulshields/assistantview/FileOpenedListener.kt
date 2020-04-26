package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import org.koin.core.KoinComponent
import org.koin.core.inject

class FileOpenedListener : FileEditorManagerListener, KoinComponent {
    private val fileAssistantService: FileAssistantService by inject()
    private val fileManagerService: FileManagerService by inject()
    private val assistantViewService: AssistantViewService by inject()

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val currentlyOpenedFile = event.newFile ?: return
        val project = event.manager.project

        val counterpartFile = fileManagerService.getFileFromProject(currentlyOpenedFile, project)
            ?.let { fileAssistantService.getCounterpartFile(it, project) } ?: return

        assistantViewService.openFile(counterpartFile)
    }
}
