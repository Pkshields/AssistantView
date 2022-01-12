package dev.paulshields.assistantview.factories

import assertk.assertThat
import assertk.assertions.isInstanceOf
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import dev.paulshields.assistantview.AssistantView
import dev.paulshields.assistantview.testcommon.relaxedMock
import org.junit.jupiter.api.Test

class AssistantViewFactoryTest {
    private val codeEditorDocumentFactory = relaxedMock<CodeEditorDocumentFactory>()
    private val toolWindowContentFactory = relaxedMock<ToolWindowContentFactory>()
    private val codeEditorFactory = relaxedMock<CodeEditorFactory>()

    private val target = AssistantViewFactory(codeEditorDocumentFactory, toolWindowContentFactory, codeEditorFactory)

    @Test
    fun `should create assistant view`() {
        val toolWindow = relaxedMock<ToolWindow>()
        val project = relaxedMock<Project>()

        val result = target.createAssistantView(toolWindow, project)

        assertThat(result).isInstanceOf(AssistantView::class)
    }
}
