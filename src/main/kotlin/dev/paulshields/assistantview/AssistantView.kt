package dev.paulshields.assistantview

import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import dev.paulshields.assistantview.factories.CodeEditorDocumentFactory
import dev.paulshields.assistantview.factories.CodeEditorFactory
import dev.paulshields.assistantview.factories.ToolWindowContentFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AssistantView : ToolWindowFactory, KoinComponent {
    private val codeEditorFactory: CodeEditorFactory by inject()
    private val codeEditorDocumentFactory: CodeEditorDocumentFactory by inject()
    private val toolWindowContentFactory: ToolWindowContentFactory by inject()

    private lateinit var toolWindow: ToolWindow
    private lateinit var project: Project

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.toolWindow = toolWindow
        this.project = project

        val document = codeEditorDocumentFactory.createDocument("Empty File")
        val editor = codeEditorFactory.createEditor(document, PlainTextFileType.INSTANCE, project)

        val editorContent = toolWindowContentFactory.createContent(editor.component)
        toolWindow.contentManager.addContent(editorContent)
    }
}
