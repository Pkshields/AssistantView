package dev.paulshields.assistantview.factories

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class DocumentFactory {
    private val editorFactory = EditorFactory.getInstance()

    fun getDocument(file: AssistantViewFile, project: Project): Document? {
        val documentManager = PsiDocumentManager.getInstance(project)
        return documentManager.getDocument(file.underlyingPsiFile)
    }

    fun createDocument(text: String) = editorFactory.createDocument(text)
}
