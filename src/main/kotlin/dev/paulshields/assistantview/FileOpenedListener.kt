package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener

class FileOpenedListener : FileEditorManagerListener {
    override fun selectionChanged(event: FileEditorManagerEvent) {
        println(event.newFile.name)
    }
}
