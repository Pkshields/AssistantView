package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.paulshields.assistantview.services.AssistantViewEventService
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.mockKoinApplication
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest

class FileOpenedListenerTest : KoinTest {
    private val fileEditorManagerEvent = mock<FileEditorManagerEvent>()

    private val assistantViewEventService = mock<AssistantViewEventService>()

    @JvmField
    @RegisterExtension
    val dependencyInjector = mockKoinApplication(
        module {
            single { assistantViewEventService }
        }
    )

    private val target = FileOpenedListener()

    @Test
    fun `should send file opened event to the assistant view event service`() {
        val rawFile = mock<VirtualFile>()
        val project = mock<Project>()
        every { fileEditorManagerEvent.newFile } returns rawFile
        every { fileEditorManagerEvent.manager.project } returns project

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 1) { assistantViewEventService.handleFileOpenedEvent(rawFile, project) }
    }

    @Test
    fun `should ignore file opened event if new file information is not included`() {
        every { fileEditorManagerEvent.newFile } returns null

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { assistantViewEventService.handleFileOpenedEvent(any(), any()) }
    }
}
