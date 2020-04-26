package dev.paulshields.assistantview

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import dev.paulshields.assistantview.factories.CodeEditorFactory
import dev.paulshields.assistantview.factories.DocumentFactory
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.testcommon.MockJComponent
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class AssistantViewTest : KoinTest {
    private val codeEditorFactory = mock<CodeEditorFactory>()
    private val documentFactory = mock<DocumentFactory>()
    private val assistantViewService = mock<AssistantViewService>()
    private val toolWindowComponent = mock<MockJComponent>()
    private val toolWindow = mock<ToolWindow>().apply {
        every { component } returns toolWindowComponent
    }
    private val project = mock<Project>()

    private val mockAppModule = module {
        single { codeEditorFactory }
        single { documentFactory }
        single { assistantViewService }
    }

    private val target = AssistantView()

    @Before
    fun `before each test`() {
        startKoin { modules(mockAppModule) }
    }

    @After
    fun `after each test`() = stopKoin()

    @Test
    fun `test should initialize assistant view with a dummy document`() {
        val document = mock<Document>()
        val editorComponent = mock<MockJComponent>()
        val editor = createEditorWithJComponent(editorComponent)
        givenToolWindowHasBeenSetUp(document, editor)

        target.createToolWindowContent(project, toolWindow)

        verify { documentFactory.createDocument(any()) }
        verify { codeEditorFactory.createEditor(document, any(), project) }
        verify { toolWindowComponent.add(editorComponent) }
    }

    @Test
    fun `test should register assistant view with assistant view service upon initialization`() {
        givenToolWindowHasBeenSetUp()

        target.createToolWindowContent(project, toolWindow)

        verify { assistantViewService.registerAssistantView(target) }
    }

    @Test
    fun `test should open file in new code editor`() {
        val file = mock<AssistantViewFile>()
        val newEditorForFile = createEditorWithJComponent()
        givenToolWindowHasBeenSetUp()
        every { codeEditorFactory.createEditor(file, project) } returns newEditorForFile

        target.openFile(file)

        verify { codeEditorFactory.createEditor(file, project) }
    }

    @Test
    fun `test should replace old editor with new editor when opening a file`() {
        val file = mock<AssistantViewFile>()
        val newEditorComponent = mock<MockJComponent>()
        val newEditorForFile = createEditorWithJComponent(newEditorComponent)
        givenToolWindowHasBeenSetUp()
        every { codeEditorFactory.createEditor(file, project) } returns newEditorForFile

        target.openFile(file)

        verify { toolWindowComponent.remove(any<MockJComponent>()) }
        verify { toolWindowComponent.add(newEditorComponent) }
    }

    @Test
    fun `test should not change open editor when file can not be opened`() {
        val file = mock<AssistantViewFile>()
        givenToolWindowHasBeenSetUp()
        every { codeEditorFactory.createEditor(any(), project) } returns null

        target.openFile(file)

        verify(exactly = 0) { toolWindowComponent.remove(any<MockJComponent>()) }
        verify(exactly = 1) { toolWindowComponent.add(any<MockJComponent>()) }
    }

    private fun givenToolWindowHasBeenSetUp() = givenToolWindowHasBeenSetUp(mock(), createEditorWithJComponent())

    private fun givenToolWindowHasBeenSetUp(document: Document, editor: Editor) {
        every { documentFactory.createDocument(any()) } returns document
        every { codeEditorFactory.createEditor(document, any(), project) } returns editor
        every { toolWindowComponent.add(any<MockJComponent>()) } returns toolWindowComponent

        target.createToolWindowContent(project, toolWindow)
    }

    private fun createEditorWithJComponent(jComponent: MockJComponent? = null) = mock<Editor>().apply {
        every { component } returns (jComponent ?: mock<MockJComponent>())
    }
}
