package dev.paulshields.assistantview

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import dev.paulshields.assistantview.factories.CodeEditorFactory
import dev.paulshields.assistantview.factories.ToolWindowUIFactory
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.lok.logDebug
import dev.paulshields.lok.logInfo

class AssistantView(
    private val toolWindowUIFactory: ToolWindowUIFactory,
    private val codeEditorFactory: CodeEditorFactory,
    private val toolWindow: ToolWindow,
    project: Project) : Disposable {

    var currentFile: AssistantViewFile? = null
        private set

    private var editor: Editor? = null

    init {
        Disposer.register(project, this)
        addDefaultAssistantViewContentToToolWindow()
    }

    fun openFile(assistantViewFile: AssistantViewFile) {
        logDebug { "Opening ${assistantViewFile.name} in Assistant View for project ${assistantViewFile.project.name}." }

        codeEditorFactory.createEditor(assistantViewFile)?.let {
            openNewEditor(it)
        }
        currentFile = assistantViewFile
    }

    fun reset() {
        logInfo { "Resetting Assistant View for project ${currentFile?.project?.name}." }

        clearToolWindow()
        addDefaultAssistantViewContentToToolWindow()
        currentFile = null
        editor = null
    }

    private fun addDefaultAssistantViewContentToToolWindow() =
        toolWindow.contentManager.addContent(toolWindowUIFactory.createStartupAssistantViewContent())

    private fun openNewEditor(newEditor: Editor) {
        clearToolWindow()

        val editorContent = toolWindowUIFactory.createContentForCodeEditor(newEditor)
        toolWindow.contentManager.addContent(editorContent)

        editor = newEditor
    }

    private fun clearToolWindow() {
        toolWindow.contentManager.removeAllContents(true)
        editor?.let { codeEditorFactory.destroyEditor(it) }
    }

    override fun dispose() {
        clearToolWindow()
    }
}
