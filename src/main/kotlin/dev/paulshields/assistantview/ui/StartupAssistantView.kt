package dev.paulshields.assistantview.ui

import com.intellij.util.ui.JBUI
import java.awt.GridBagLayout
import javax.swing.JLabel
import javax.swing.JPanel

object StartupAssistantView {
    val panel = JPanel().apply {
        add(JLabel("Assistant View is ready!").apply {
            foreground = JBUI.CurrentTheme.ContextHelp.FOREGROUND
        })
        layout = GridBagLayout()
    }
}
