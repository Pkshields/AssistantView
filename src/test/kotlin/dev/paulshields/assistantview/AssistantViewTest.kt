package dev.paulshields.assistantview

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.Content
import dev.paulshields.assistantview.factories.CodeEditorFactory
import dev.paulshields.assistantview.factories.ToolWindowUIFactory
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

class AssistantViewTest {
    private val assistantViewFile = mock<AssistantViewFile>()
    private val editor = mock<Editor>()
    private val content = mock<Content>()

    private val toolWindow = mock<ToolWindow>()
    private val project = mock<Project>()
    private val toolWindowUIFactory = mock<ToolWindowUIFactory>().apply {
        every { createContentForCodeEditor(editor) } returns content
    }
    private val codeEditorFactory = mock<CodeEditorFactory>().apply {
        every { createEditor(assistantViewFile) } returns editor
    }

    private val target = AssistantView(toolWindowUIFactory, codeEditorFactory, toolWindow, project)

    @Test
    fun `should start assistant view with startup view`() {
        val expectedContent = mock<Content>()
        every { toolWindowUIFactory.createStartupAssistantViewContent() } returns expectedContent

        val target = AssistantView(toolWindowUIFactory, codeEditorFactory, toolWindow, project)

        verify { toolWindow.contentManager.addContent(expectedContent) }
    }

    @Test
    fun `should get editor for opened file`() {
        target.openFile(assistantViewFile)

        verify { codeEditorFactory.createEditor(assistantViewFile) }
    }

    @Test
    fun `should open editor for opened file in tool window`() {
        target.openFile(assistantViewFile)

        verify {
            toolWindowUIFactory.createContentForCodeEditor(editor)
            toolWindow.contentManager.addContent(content)
        }
    }

    @Test
    fun `should destroy old editor when opening new file`() {
        target.openFile(assistantViewFile)

        target.openFile(assistantViewFile)

        verify {
            toolWindow.contentManager.removeAllContents(any())
            codeEditorFactory.destroyEditor(editor)
        }
    }

    @Test
    fun `should not destroy old editor when new file can not be opened correctly`() {
        every { codeEditorFactory.createEditor(assistantViewFile) } returns null

        target.openFile(assistantViewFile)

        val mockContentManager = toolWindow.contentManager
        verify(exactly = 0) {
            mockContentManager.removeAllContents(any())
            codeEditorFactory.destroyEditor(editor)
        }
    }

    @Test
    fun `should destroy editor when assistant view is disposed`() {
        target.openFile(assistantViewFile)

        target.dispose()

        verify { codeEditorFactory.destroyEditor(editor) }
    }
}
