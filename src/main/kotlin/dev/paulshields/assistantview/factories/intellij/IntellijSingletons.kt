package dev.paulshields.assistantview.factories.intellij

import com.intellij.openapi.editor.EditorFactory
import com.intellij.ui.content.ContentFactory

class IntellijSingletons {
    val editorFactory: EditorFactory = EditorFactory.getInstance()
    val contentFactory: ContentFactory = ContentFactory.getInstance()
}
