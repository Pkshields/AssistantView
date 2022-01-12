package dev.paulshields.assistantview.factories

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import dev.paulshields.assistantview.AssistantView

class AssistantViewFactory(
    private val codeEditorDocumentFactory: CodeEditorDocumentFactory,
    private val toolWindowContentFactory: ToolWindowContentFactory,
    private val codeEditorFactory: CodeEditorFactory) {

    fun createAssistantView(toolWindow: ToolWindow, project: Project) =
        AssistantView(codeEditorDocumentFactory, toolWindowContentFactory, codeEditorFactory, toolWindow, project)
}
