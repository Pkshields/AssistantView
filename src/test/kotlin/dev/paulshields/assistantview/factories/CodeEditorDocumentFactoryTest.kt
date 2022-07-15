package dev.paulshields.assistantview.factories

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class CodeEditorDocumentFactoryTest {
    private val document = mock<Document>()
    private val psiFile = mock<PsiFile>()
    private val psiDocumentManager = mock<PsiDocumentManager>()
    private val project = mock<Project>().apply {
        every { getService(PsiDocumentManager::class.java) } returns psiDocumentManager
    }
    private val assistantViewFile = mock<AssistantViewFile>().apply {
        every { project } returns this@CodeEditorDocumentFactoryTest.project
        every { psiFile } returns this@CodeEditorDocumentFactoryTest.psiFile
    }

    private val target = CodeEditorDocumentFactory()

    @Test
    fun `should create code editor from assistant view file`() {
        every { psiDocumentManager.getDocument(psiFile) } returns document

        val result = target.getEditorDocument(assistantViewFile)

        assertThat(result).isEqualTo(document)
    }

    @Test
    fun `should not create code editor from assistant view file if no associated document`() {
        every { psiDocumentManager.getDocument(psiFile) } returns null

        val result = target.getEditorDocument(assistantViewFile)

        assertThat(result).isNull()
    }
}
