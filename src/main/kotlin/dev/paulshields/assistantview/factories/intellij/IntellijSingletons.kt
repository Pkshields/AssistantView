package dev.paulshields.assistantview.factories.intellij

import com.intellij.openapi.editor.EditorFactory
import com.intellij.ui.content.ContentFactory

class IntellijSingletons {
    val editorFactory = EditorFactory.getInstance()
    val contentFactory = ContentFactory.SERVICE.getInstance()
}
