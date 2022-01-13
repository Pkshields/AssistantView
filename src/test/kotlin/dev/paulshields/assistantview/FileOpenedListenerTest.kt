package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.mockKoinApplication
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest

class FileOpenedListenerTest : KoinTest {
    private val assistantViewFile = mock<AssistantViewFile>()
    private val virtualFile = mock<VirtualFile>()
    private val project = mock<Project>()
    private val fileEditorManagerEvent = mock<FileEditorManagerEvent>().apply {
        every { newFile } returns virtualFile
        every { manager.project } returns project
    }

    private val fileAssistantService = mock<FileAssistantService>().apply {
        every { getCounterpartFile(assistantViewFile) } returns assistantViewFile
    }
    private val fileManagerService = mock<FileManagerService>().apply {
        every { getFileFromVirtualFile(virtualFile, project) } returns assistantViewFile
    }
    private val assistantViewService = mock<AssistantViewService>()

    @JvmField
    @RegisterExtension
    val dependencyInjector = mockKoinApplication(
        module {
            single { fileAssistantService }
            single { fileManagerService }
            single { assistantViewService }
        }
    )

    private val target = FileOpenedListener()

    @Test
    fun `should get assistant view file for newly opened file`() {
        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 1) { fileManagerService.getFileFromVirtualFile(virtualFile, project) }
    }

    @Test
    fun `should handle if no newly opened file is provided`() {
        every { fileEditorManagerEvent.newFile } returns null

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { fileManagerService.getFileFromVirtualFile(any(), any()) }
    }

    @Test
    fun `should get counterpart file for newly opened file`() {
        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 1) { fileAssistantService.getCounterpartFile(assistantViewFile) }
    }

    @Test
    fun `should handle if newly opened file can not be processed`() {
        every { fileManagerService.getFileFromVirtualFile(virtualFile, project) } returns null

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { fileAssistantService.getCounterpartFile(any()) }
    }

    @Test
    fun `should open counterpart file`() {
        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 1) { assistantViewService.openFile(assistantViewFile) }
    }

    @Test
    fun `should handle if counterpart file does not exist`() {
        every { fileAssistantService.getCounterpartFile(assistantViewFile) } returns null

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { assistantViewService.openFile(any()) }
    }
}
