package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.AssistantView
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

class AssistantViewServiceTest {
    private val assistantView = mock<AssistantView>()
    private val project = mock<Project>()
    private val assistantViewFile = mock<AssistantViewFile>().apply {
        every { this@apply.project } returns this@AssistantViewServiceTest.project
    }

    private val target = AssistantViewService()

    @Test
    fun `should register new assistant view in service and open file`() {
        target.registerAssistantViewForProject(assistantView, project)
        target.openFile(assistantViewFile)

        verify(exactly = 1) { assistantView.openFile(assistantViewFile) }
    }

    @Test
    fun `should support overwriting assistant view associated with project`() {
        val secondAssistantView = mock<AssistantView>()

        target.registerAssistantViewForProject(assistantView, project)
        target.registerAssistantViewForProject(secondAssistantView, project)
        target.openFile(assistantViewFile)

        verify(exactly = 0) { assistantView.openFile(assistantViewFile) }
        verify(exactly = 1) { secondAssistantView.openFile(assistantViewFile) }
    }

    @Test
    fun `should remove assistant view from service when project is closed`() {
        target.registerAssistantViewForProject(assistantView, project)
        target.closeAssistantViewForProject(project)
        target.openFile(assistantViewFile)

        verify(exactly = 0) { assistantView.openFile(assistantViewFile) }
    }

    @Test
    fun `should handle removing assistant view that is not registered with service`() {
        target.closeAssistantViewForProject(project)

        // assertThat(no crash occurred)
    }

    @Test
    fun `should handle trying to open file for project that does not have an assistant view opened`() {
        target.openFile(assistantViewFile)

        // assertThat(no crash occurred)
    }
}
