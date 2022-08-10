package dev.paulshields.assistantview

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.events.VFileCopyEvent
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent
import com.intellij.openapi.vfs.newvfs.events.VFileMoveEvent
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileManagerService
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.mockKoinApplication
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest

class FileDeletedListenerTest : KoinTest {
    private val virtualFile = mock<VirtualFile>()
    private val assistantViewFile = mock<AssistantViewFile>()
    private val deletedFileEvent = mock<VFileDeleteEvent>().apply {
        every { file } returns virtualFile
    }

    private val assistantViewService = mock<AssistantViewService>()
    private val fileManagerService = mock<FileManagerService>().apply {
        every { getFileFromVirtualFile(virtualFile) } returns assistantViewFile
    }

    @JvmField
    @RegisterExtension
    val dependencyInjector = mockKoinApplication(
        module {
            single { fileManagerService }
            single { assistantViewService }
        }
    )

    private val target = FileDeletedListener()

    @Test
    fun `should close file in assistant view when file is deleted`() {
        target.before(listOf(deletedFileEvent))

        verify { assistantViewService.closeFile(assistantViewFile) }
    }

    @Test
    fun `should not try to close file if trying to get associated assistant view file fails`() {
        every { fileManagerService.getFileFromVirtualFile(virtualFile) } returns null

        target.before(listOf(deletedFileEvent))

        verify(exactly = 0) { assistantViewService.closeFile(any()) }
    }

    @Test
    fun `should ignore all non deleted file events`() {
        val copyEvent = mock<VFileCopyEvent>().apply {
            every { file } returns virtualFile
        }
        val createEvent = mock<VFileCreateEvent>().apply {
            every { file } returns virtualFile
        }
        val moveEvent = mock<VFileMoveEvent>().apply {
            every { file } returns virtualFile
        }

        target.before(listOf(copyEvent, createEvent, moveEvent))

        verify(exactly = 0) { assistantViewService.closeFile(any()) }
    }
}
