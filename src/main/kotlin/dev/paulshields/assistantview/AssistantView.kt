package dev.paulshields.assistantview

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import dev.paulshields.assistantview.factories.CodeEditorFactory
import dev.paulshields.assistantview.factories.DocumentFactory
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject

class AssistantView : ToolWindowFactory, KoinComponent {
    private val codeEditorFactory: CodeEditorFactory by inject()
    private val documentFactory: DocumentFactory by inject()

    private lateinit var toolWindow: ToolWindow
    private lateinit var project: Project

    private var currentEditor: Editor? = null

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.toolWindow = toolWindow
        this.project = project

        val document = documentFactory.createDocument("Empty File")
        val editor = codeEditorFactory.createEditor(document, PlainTextFileType.INSTANCE, project)
        swapEditorInView(editor)

        registerWithAssistantViewService()
    }

    fun openFile(file: AssistantViewFile) {
        codeEditorFactory.createEditor(file, project)?.let {
            swapEditorInView(it)
        }
    }

    private fun swapEditorInView(editor: Editor) {
        currentEditor?.let { toolWindow.component.remove(it.component) }
        toolWindow.component.add(editor.component)
        toolWindow.component.updateUI()
        currentEditor = editor
    }

    private fun registerWithAssistantViewService() {
        val assistantViewService = get<AssistantViewService>()
        assistantViewService.registerAssistantView(this)
    }
}
