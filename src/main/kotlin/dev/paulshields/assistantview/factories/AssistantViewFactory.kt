package dev.paulshields.assistantview.factories

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import dev.paulshields.assistantview.AssistantView

class AssistantViewFactory(
    private val toolWindowUIFactory: ToolWindowUIFactory,
    private val codeEditorFactory: CodeEditorFactory) {

    fun createAssistantView(toolWindow: ToolWindow, project: Project) =
        AssistantView(toolWindowUIFactory, codeEditorFactory, toolWindow, project)
}
