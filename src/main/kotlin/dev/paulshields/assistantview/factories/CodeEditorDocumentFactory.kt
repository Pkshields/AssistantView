package dev.paulshields.assistantview.factories

import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiDocumentManager
import dev.paulshields.assistantview.lang.AssistantViewFile

class CodeEditorDocumentFactory {
    fun getEditorDocument(file: AssistantViewFile): Document? {
        val documentManager = PsiDocumentManager.getInstance(file.project)
        return documentManager.getDocument(file.psiFile)
    }
}
