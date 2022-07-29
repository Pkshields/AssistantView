package dev.paulshields.assistantview.services

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.paulshields.assistantview.common.Dispatcher
import dev.paulshields.assistantview.extensions.isDumb
import dev.paulshields.assistantview.extensions.runOnUiThread
import dev.paulshields.assistantview.extensions.runWithReadPermission
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest

class AssistantViewEventServiceTest : KoinTest {
    private val assistantViewFile = mock<AssistantViewFile>()
    private val rawFile = mock<VirtualFile>()
    private val dumbService = mock<DumbService>().apply {
        every { runWhenSmart(any()) } answers { firstArg<Runnable>().run() }
    }
    private val fileEditorManager = mock<FileEditorManager>().apply {
        every { selectedEditor?.file } returns rawFile
    }
    private val project = mock<Project>().apply {
        mockkStatic(Project::isDumb)
        every { isDumb } returns false
        every { getService(DumbService::class.java) } returns dumbService
        every { getComponent(FileEditorManager::class.java) } returns fileEditorManager
    }

    private val assistantViewService = mock<AssistantViewService>().apply {
        every { assistantViewExistsForProject(project) } returns true
    }
    private val counterpartFileService = mock<CounterpartFileService>().apply {
        every { findCounterpartFile(assistantViewFile) } returns assistantViewFile
    }
    private val fileManagerService = mock<FileManagerService>().apply {
        every { getFileFromVirtualFile(rawFile, project) } returns assistantViewFile
    }
    private val dispatcher = mock<Dispatcher>().apply {
        every { runOnBackgroundThread(any()) } answers { firstArg<() -> Unit>().invoke() }
    }

    private val target = AssistantViewEventService(counterpartFileService, fileManagerService, assistantViewService, dispatcher)

    @BeforeEach
    fun beforeEach() {
        mockIntellijThreadControlFunctions()
    }

    @Test
    fun `should get assistant view file for newly opened file`() {
        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 1) { fileManagerService.getFileFromVirtualFile(rawFile, project) }
    }

    @Test
    fun `should get assistant view file on a background thread`() {
        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 1) { dispatcher.runOnBackgroundThread(any()) }
    }

    @Test
    fun `should find counterpart file for newly opened file`() {
        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 1) { counterpartFileService.findCounterpartFile(assistantViewFile) }
    }

    @Test
    fun `should handle if newly opened file can not be processed`() {
        every { fileManagerService.getFileFromVirtualFile(rawFile, project) } returns null

        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 0) { counterpartFileService.findCounterpartFile(any()) }
    }

    @Test
    fun `should open counterpart file`() {
        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 1) { assistantViewService.openFile(assistantViewFile) }
    }

    @Test
    fun `should handle if counterpart file does not exist`() {
        every { counterpartFileService.findCounterpartFile(assistantViewFile) } returns null

        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 0) { assistantViewService.openFile(any()) }
    }

    @Test
    fun `should ignore file opened event if no assistant view window is available for project`() {
        every { assistantViewService.assistantViewExistsForProject(project) } returns false

        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 0) { assistantViewService.openFile(any()) }
    }

    @Test
    fun `should not wait for dumb mode to finish if not in dumb mode`() {
        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 0) { dumbService.runWhenSmart(any()) }
    }

    @Test
    fun `should wait for dumb mode to finish before opening counterpart file`() {
        every { project.isDumb } returns true

        target.handleFileOpenedEvent(rawFile, project)

        verify(ordering = Ordering.ORDERED) {
            dumbService.runWhenSmart(any())
            assistantViewService.openFile(assistantViewFile)
        }
    }

    @Test
    fun `should wait for dumb mode to finish before checking if assistant view window is available`() {
        every { project.isDumb } returns true

        target.handleFileOpenedEvent(rawFile, project)

        verify(ordering = Ordering.ORDERED) {
            dumbService.runWhenSmart(any())
            assistantViewService.assistantViewExistsForProject(project)
        }
    }

    @Test
    fun `should not try to open find or open counterpart file until dumb mode is finished`() {
        every { project.isDumb } returns true
        every { dumbService.runWhenSmart(any()) } answers { }

        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 0) { assistantViewService.openFile(any()) }
    }

    @Test
    fun `should not try to open find or open counterpart file when dumb mode is finished if no editor is currently open`() {
        every { project.isDumb } returns true
        every { fileEditorManager.selectedEditor } returns null

        target.handleFileOpenedEvent(rawFile, project)

        verify(exactly = 0) { assistantViewService.openFile(any()) }
    }

    private fun mockIntellijThreadControlFunctions() {
        mockkStatic("dev.paulshields.assistantview.extensions.IntellijThreadControlExtensionsKt")
        every { runWithReadPermission(any<() -> AssistantViewFile?>()) } answers { firstArg<() -> AssistantViewFile?>().invoke() }
        every { runOnUiThread(any()) } answers { firstArg<() -> Unit>().invoke() }
    }
}
