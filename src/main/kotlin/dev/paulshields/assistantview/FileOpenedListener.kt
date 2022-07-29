package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import dev.paulshields.assistantview.services.AssistantViewEventService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FileOpenedListener : FileEditorManagerListener, KoinComponent {
    private val assistantViewEventService: AssistantViewEventService by inject()

    override fun selectionChanged(event: FileEditorManagerEvent) {
        assistantViewEventService.handleFileOpenedEvent(event)
    }
}
