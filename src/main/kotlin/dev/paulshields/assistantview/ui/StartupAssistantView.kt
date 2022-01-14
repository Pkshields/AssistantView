package dev.paulshields.assistantview.ui

import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import com.intellij.util.ui.JBUI

object StartupAssistantView {
    val panel = panel {
        row {
            text("Assistant View is ready!")
                .horizontalAlign(HorizontalAlign.CENTER)
                .verticalAlign(VerticalAlign.CENTER)
                .applyToComponent { foreground = JBUI.CurrentTheme.ContextHelp.FOREGROUND }
        }.resizableRow()
    }
}
