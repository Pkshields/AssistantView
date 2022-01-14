package dev.paulshields.assistantview.factories

import com.intellij.openapi.editor.Editor
import dev.paulshields.assistantview.factories.intellij.IntellijSingletons
import dev.paulshields.assistantview.ui.StartupAssistantView
import javax.swing.JComponent

class ToolWindowUIFactory(intellijSingletons: IntellijSingletons) {
    private val contentFactory = intellijSingletons.contentFactory

    fun createContentForCodeEditor(editor: Editor) = createContent(editor.component)

    fun createStartupAssistantViewContent() = createContent(StartupAssistantView.panel)

    private fun createContent(component: JComponent) = contentFactory.createContent(component, "", false)
}
