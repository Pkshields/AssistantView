package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.paulshields.assistantview.common.Dispatcher
import dev.paulshields.assistantview.extensions.runOnUiThread
import dev.paulshields.assistantview.extensions.runWithReadPermission
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.mockKoinApplication
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.jetbrains.kotlin.idea.caches.resolve.util.isInDumbMode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest

class FileOpenedListenerTest : KoinTest {
    private val assistantViewFile = mock<AssistantViewFile>()
    private val virtualFile = mock<VirtualFile>()
    private val dumbService = mock<DumbService>().apply {
        every { runWhenSmart(any()) } answers { firstArg<Runnable>().run() }
    }
    private val project = mock<Project>().apply {
        mockkStatic(Project::isInDumbMode)
        every { isInDumbMode() } returns false
        every { getService(DumbService::class.java) } returns dumbService
    }
    private val fileEditorManager = mock<FileEditorManager>().apply {
        every { project } returns this@FileOpenedListenerTest.project
        every { selectedEditor?.file } returns virtualFile
    }
    private val fileEditorManagerEvent = mock<FileEditorManagerEvent>().apply {
        every { newFile } returns virtualFile
        every { manager } returns fileEditorManager
    }

    private val assistantViewService = mock<AssistantViewService>()
    private val fileAssistantService = mock<FileAssistantService>().apply {
        every { getCounterpartFile(assistantViewFile) } returns assistantViewFile
    }
    private val fileManagerService = mock<FileManagerService>().apply {
        every { getFileFromVirtualFile(virtualFile, project) } returns assistantViewFile
    }
    private val dispatcher = mock<Dispatcher>().apply {
        every { runOnBackgroundThread(any()) } answers { firstArg<() -> Unit>().invoke() }
    }

    @JvmField
    @RegisterExtension
    val dependencyInjector = mockKoinApplication(
        module {
            single { fileAssistantService }
            single { fileManagerService }
            single { assistantViewService }
            single { dispatcher }
        }
    )

    private val target = FileOpenedListener()

    @BeforeEach
    fun beforeEach() {
        mockIntellijThreadControlFunctions()
    }

    @Test
    fun `should get assistant view file for newly opened file`() {
        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 1) { fileManagerService.getFileFromVirtualFile(virtualFile, project) }
    }

    @Test
    fun `should get assistant view file on a background thread`() {
        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 1) { dispatcher.runOnBackgroundThread(any()) }
    }

    @Test
    fun `should handle if no newly opened file is provided`() {
        every { fileEditorManagerEvent.newFile } returns null

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { fileManagerService.getFileFromVirtualFile(any(), any()) }
    }

    @Test
    fun `should not invoke the dispatch thread if no newly opened file is provided`() {
        every { fileEditorManagerEvent.newFile } returns null

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { dispatcher.runOnBackgroundThread(any()) }
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

    @Test
    fun `should not wait for dumb mode to finish if not in dumb mode`() {
        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { dumbService.runWhenSmart(any()) }
    }

    @Test
    fun `should wait for dumb mode to finish before opening counterpart file`() {
        every { project.isInDumbMode() } returns true

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 1) {
            dumbService.runWhenSmart(any())
            assistantViewService.openFile(assistantViewFile)
        }
    }

    @Test
    fun `should not try to open find or open counterpart file until dumb mode is finished`() {
        every { project.isInDumbMode() } returns true
        every { dumbService.runWhenSmart(any()) } answers { }

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { assistantViewService.openFile(assistantViewFile) }
    }

    @Test
    fun `should not try to open find or open counterpart file when dumb mode is finished if no editor is currently open`() {
        every { project.isInDumbMode() } returns true
        every { fileEditorManager.selectedEditor } returns null

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 0) { assistantViewService.openFile(assistantViewFile) }
    }

    private fun mockIntellijThreadControlFunctions() {
        mockkStatic("dev.paulshields.assistantview.extensions.IntellijThreadControlExtensionsKt")
        every { runWithReadPermission(any<() -> AssistantViewFile?>()) } answers { firstArg<() -> AssistantViewFile?>().invoke() }
        every { runOnUiThread(any()) } answers { firstArg<() -> Unit>().invoke() }
    }
}
