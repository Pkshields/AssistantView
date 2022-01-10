package dev.paulshields.assistantview.factories

import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiDocumentManager
import dev.paulshields.assistantview.factories.intellij.IntellijSingletons
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class CodeEditorDocumentFactory(intellijSingletons: IntellijSingletons) {
    private val editorFactory = intellijSingletons.editorFactory

    fun getEditorDocument(file: AssistantViewFile): Document? {
        val documentManager = PsiDocumentManager.getInstance(file.project)
        return documentManager.getDocument(file.psiFile)
    }

    fun createDocument(text: String) = editorFactory.createDocument(text)
}
