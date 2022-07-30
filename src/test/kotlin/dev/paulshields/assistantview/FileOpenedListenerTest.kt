package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.paulshields.assistantview.services.FileOpenerService
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

    private val fileOpenerService = mock<FileOpenerService>()

    @JvmField
    @RegisterExtension
    val dependencyInjector = mockKoinApplication(
        module {
            single { fileOpenerService }
        }
    )

    private val target = FileOpenedListener()

    @Test
    fun `should open counterpart file when file opened event is received`() {
        val rawFile = mock<VirtualFile>()
        val project = mock<Project>()
        every { fileEditorManagerEvent.newFile } returns rawFile
        every { fileEditorManagerEvent.manager.project } returns project

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 1) { fileOpenerService.openCounterpartForFile(rawFile, project) }
    }

    @Test
    fun `should ignore file opened event if new file information is not included`() {
        every { fileEditorManagerEvent.newFile } returns null

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { fileOpenerService.openCounterpartForFile(any(), any()) }
    }
}
