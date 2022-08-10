package dev.paulshields.assistantview.services

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.AssistantView
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

class AssistantViewServiceTest {
    private val project = mock<Project>()
    private val fileToOpen = mock<AssistantViewFile>().apply {
        every { this@apply.project } returns this@AssistantViewServiceTest.project
    }
    private val otherFile = mock<AssistantViewFile>().apply {
        every { this@apply.project } returns this@AssistantViewServiceTest.project
    }
    private val assistantView = mock<AssistantView>().apply {
        every { currentFile } returns fileToOpen
    }

    private val target = AssistantViewService()

    @Test
    fun `should register new assistant view in service and open file`() {
        target.registerAssistantViewForProject(assistantView, project)
        target.openFile(fileToOpen)

        verify(exactly = 1) { assistantView.openFile(fileToOpen) }
    }

    @Test
    fun `should support overwriting assistant view associated with project`() {
        val secondAssistantView = mock<AssistantView>()

        target.registerAssistantViewForProject(assistantView, project)
        target.registerAssistantViewForProject(secondAssistantView, project)
        target.openFile(fileToOpen)

        verify(exactly = 0) { assistantView.openFile(fileToOpen) }
        verify(exactly = 1) { secondAssistantView.openFile(fileToOpen) }
    }

    @Test
    fun `should remove assistant view from service when project is closed`() {
        target.registerAssistantViewForProject(assistantView, project)
        target.closeAssistantViewForProject(project)
        target.openFile(fileToOpen)

        verify(exactly = 0) { assistantView.openFile(fileToOpen) }
    }

    @Test
    fun `should handle removing assistant view that is not registered with service`() {
        target.closeAssistantViewForProject(project)

        // assertThat(no crash occurred)
    }

    @Test
    fun `should handle trying to open file for project that does not have an assistant view opened`() {
        target.openFile(fileToOpen)

        // assertThat(no crash occurred)
    }

    @Test
    fun `should return true if assistant view is registered for project`() {
        target.registerAssistantViewForProject(assistantView, project)

        val result = target.assistantViewExistsForProject(project)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if no assistant view is registered for project`() {
        val result = target.assistantViewExistsForProject(project)

        assertThat(result).isFalse()
    }

    @Test
    fun `should close file that is open in assistant view`() {
        target.registerAssistantViewForProject(assistantView, project)
        target.openFile(fileToOpen)

        target.closeFile(fileToOpen)

        verify(exactly = 1) { assistantView.reset() }
    }

    @Test
    fun `should ignore close file request if file is not opened`() {
        target.registerAssistantViewForProject(assistantView, project)
        target.openFile(fileToOpen)

        target.closeFile(otherFile)

        verify(exactly = 0) { assistantView.reset() }
    }

    @Test
    fun `should ignore close file request if no assistant view exists for project`() {
        target.closeFile(fileToOpen)

        // assertThat(Nothing happens, as expected)
    }
}
