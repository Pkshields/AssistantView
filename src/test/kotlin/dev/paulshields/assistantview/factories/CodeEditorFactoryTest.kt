package dev.paulshields.assistantview.factories

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.factories.intellij.IntellijSingletons
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

class CodeEditorFactoryTest {
    private val project = mock<Project>()
    private val fileType = PlainTextFileType.INSTANCE
    private val assistantViewFile = mock<AssistantViewFile>()
    private val editor = mock<Editor>()
    private val document = mock<Document>()
    private val codeEditorDocumentFactory = mock<CodeEditorDocumentFactory>()
    private val editorFactory = mock<EditorFactory>().apply {
        every { createEditor(document, project, fileType, any()) } returns editor
    }
    private val intellijSingletons = mock<IntellijSingletons>().apply {
        every { editorFactory } returns this@CodeEditorFactoryTest.editorFactory
    }

    private val target = CodeEditorFactory(codeEditorDocumentFactory, intellijSingletons)

    @Test
    fun `should create editor from assistant view file`() {
        every { codeEditorDocumentFactory.getEditorDocument(assistantViewFile) } returns document
        every { assistantViewFile.psiFile.fileType } returns fileType
        every { assistantViewFile.project } returns project

        val result = target.createEditor(assistantViewFile)

        assertThat(result).isEqualTo(editor)
    }

    @Test
    fun `should return null if can not create document from assistant view file`() {
        every { codeEditorDocumentFactory.getEditorDocument(assistantViewFile) } returns null

        val result = target.createEditor(assistantViewFile)

        assertThat(result).isNull()
    }

    @Test
    fun `should create editor from document`() {
        val result = target.createEditor(document, fileType, project)

        assertThat(result).isEqualTo(editor)
    }

    @Test
    fun `should destroy editor`() {
        target.destroyEditor(editor)

        verify(exactly = 1) { editorFactory.releaseEditor(editor) }
    }
}
