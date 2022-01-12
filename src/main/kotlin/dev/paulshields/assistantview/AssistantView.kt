package dev.paulshields.assistantview

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import dev.paulshields.assistantview.factories.CodeEditorDocumentFactory
import dev.paulshields.assistantview.factories.CodeEditorFactory
import dev.paulshields.assistantview.factories.ToolWindowContentFactory

class AssistantView(
    private val codeEditorDocumentFactory: CodeEditorDocumentFactory,
    private val toolWindowContentFactory: ToolWindowContentFactory,
    private val codeEditorFactory: CodeEditorFactory,
    private val toolWindow: ToolWindow,
    private val project: Project) : Disposable {

    private val editor: Editor = createEmptyEditor()

    init {
        Disposer.register(project, this)
    }

    private fun createEmptyEditor(): Editor {
        val emptyDocument = codeEditorDocumentFactory.createDocument("Empty File")
        val editor = codeEditorFactory.createEditor(emptyDocument, PlainTextFileType.INSTANCE, project)

        val editorContent = toolWindowContentFactory.createContent(editor.component)
        toolWindow.contentManager.addContent(editorContent)

        return editor
    }

    override fun dispose() {
        codeEditorFactory.destroyEditor(editor)
    }
}
