package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import dev.paulshields.assistantview.services.AssistantViewFileService
import dev.paulshields.lok.logWarn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FileOpenedListener : FileEditorManagerListener, KoinComponent {
    private val assistantViewFileService: AssistantViewFileService by inject()

    override fun selectionChanged(event: FileEditorManagerEvent) {
        if (event.newFile == null) {
            logWarn { "File Opened event was triggered, but no file information was included in the event. Ignoring..." }
            return
        }

        assistantViewFileService.openCounterpartForFile(event.newFile, event.manager.project)
    }
}
