package dev.paulshields.assistantview

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileManagerService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FileDeletedListener : BulkFileListener, KoinComponent {
    private val fileManagerService: FileManagerService by inject()
    private val assistantViewService: AssistantViewService by inject()

    override fun before(events: List<VFileEvent>) {
        events.filterIsInstance<VFileDeleteEvent>()
            .forEach { closeFileIfOpenInAssistantView(it.file) }
    }

    private fun closeFileIfOpenInAssistantView(file: VirtualFile) {
        fileManagerService.getFileFromVirtualFile(file)
            ?.let { assistantViewService.closeFile(it) }
    }
}
