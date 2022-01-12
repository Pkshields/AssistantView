package dev.paulshields.assistantview.factories

import assertk.assertThat
import assertk.assertions.isInstanceOf
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import dev.paulshields.assistantview.AssistantView
import dev.paulshields.assistantview.testcommon.mock
import org.junit.jupiter.api.Test

class AssistantViewFactoryTest {
    private val codeEditorDocumentFactory = mock<CodeEditorDocumentFactory>()
    private val toolWindowContentFactory = mock<ToolWindowContentFactory>()
    private val codeEditorFactory = mock<CodeEditorFactory>()

    private val target = AssistantViewFactory(codeEditorDocumentFactory, toolWindowContentFactory, codeEditorFactory)

    @Test
    fun `should create assistant view`() {
        val toolWindow = mock<ToolWindow>()
        val project = mock<Project>()

        val result = target.createAssistantView(toolWindow, project)

        assertThat(result).isInstanceOf(AssistantView::class)
    }
}
