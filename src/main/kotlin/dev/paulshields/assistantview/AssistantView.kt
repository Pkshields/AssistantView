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
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class AssistantView(
    private val codeEditorDocumentFactory: CodeEditorDocumentFactory,
    private val toolWindowContentFactory: ToolWindowContentFactory,
    private val codeEditorFactory: CodeEditorFactory,
    private val toolWindow: ToolWindow,
    private val project: Project) : Disposable {

    private var editor: Editor? = createEmptyEditor().also { openNewEditor(it) }

    init {
        Disposer.register(project, this)
    }

    private fun createEmptyEditor(): Editor {
        val emptyDocument = codeEditorDocumentFactory.createDocument("Empty File")
        return codeEditorFactory.createEditor(emptyDocument, PlainTextFileType.INSTANCE, project)
    }

    fun openFile(assistantViewFile: AssistantViewFile) {
        codeEditorFactory.createEditor(assistantViewFile)?.let {
            openNewEditor(it)
        }
    }

    private fun openNewEditor(newEditor: Editor) {
        toolWindow.contentManager.removeAllContents(true)

        val editorContent = toolWindowContentFactory.createContent(newEditor.component)
        toolWindow.contentManager.addContent(editorContent)

        destroyOpenEditor()
        editor = newEditor
    }

    private fun destroyOpenEditor() = editor?.let { codeEditorFactory.destroyEditor(it) }

    override fun dispose() {
        destroyOpenEditor()
    }
}
