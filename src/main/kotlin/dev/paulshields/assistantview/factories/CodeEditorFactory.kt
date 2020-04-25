package dev.paulshields.assistantview.factories

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class CodeEditorFactory(private val documentFactory: DocumentFactory) {
    private val editorFactory = EditorFactory.getInstance()

    fun createEditor(file: AssistantViewFile, project: Project) =
        documentFactory.getDocument(file, project)?.let {
            createEditor(it, file.fileType, project)
        }

    fun createEditor(document: Document, fileType: FileType, project: Project) =
        editorFactory.createEditor(document, project, fileType, false)

    fun destroyEditor(editor: Editor) = editorFactory.releaseEditor(editor)
}
