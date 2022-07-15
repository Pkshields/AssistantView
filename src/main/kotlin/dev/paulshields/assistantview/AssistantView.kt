package dev.paulshields.assistantview

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import dev.paulshields.assistantview.factories.CodeEditorFactory
import dev.paulshields.assistantview.factories.ToolWindowUIFactory
import dev.paulshields.assistantview.lang.AssistantViewFile

class AssistantView(
    private val toolWindowUIFactory: ToolWindowUIFactory,
    private val codeEditorFactory: CodeEditorFactory,
    private val toolWindow: ToolWindow,
    project: Project) : Disposable {

    private var editor: Editor? = null

    init {
        Disposer.register(project, this)
        setupStartupAssistantViewContent()
    }

    fun openFile(assistantViewFile: AssistantViewFile) {
        codeEditorFactory.createEditor(assistantViewFile)?.let {
            openNewEditor(it)
        }
    }

    private fun setupStartupAssistantViewContent() = toolWindow.contentManager.addContent(toolWindowUIFactory.createStartupAssistantViewContent())

    private fun openNewEditor(newEditor: Editor) {
        toolWindow.contentManager.removeAllContents(true)

        val editorContent = toolWindowUIFactory.createContentForCodeEditor(newEditor)
        toolWindow.contentManager.addContent(editorContent)

        destroyOpenEditor()
        editor = newEditor
    }

    private fun destroyOpenEditor() = editor?.let { codeEditorFactory.destroyEditor(it) }

    override fun dispose() {
        destroyOpenEditor()
    }
}
