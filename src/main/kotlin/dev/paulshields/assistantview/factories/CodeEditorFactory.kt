package dev.paulshields.assistantview.factories

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.factories.intellij.IntellijSingletons
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class CodeEditorFactory(private val codeEditorDocumentFactory: CodeEditorDocumentFactory, intellijSingletons: IntellijSingletons) {
    private val editorFactory = intellijSingletons.editorFactory

    fun createEditor(file: AssistantViewFile) =
        codeEditorDocumentFactory.getEditorDocument(file)?.let {
            createEditor(it, file.psiFile.fileType, file.project)
        }

    fun createEditor(document: Document, fileType: FileType, project: Project) =
        editorFactory.createEditor(document, project, fileType, false)

    fun destroyEditor(editor: Editor) = editorFactory.releaseEditor(editor)
}
