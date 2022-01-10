package dev.paulshields.assistantview.factories

import dev.paulshields.assistantview.factories.intellij.IntellijSingletons
import javax.swing.JComponent

class ToolWindowContentFactory(intellijSingletons: IntellijSingletons) {
    private val contentFactory = intellijSingletons.contentFactory

    fun createContent(component: JComponent) = contentFactory.createContent(component, "", false)
}
