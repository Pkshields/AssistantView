package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

interface AssistantViewClass {
    val project: Project
    val superClasses: List<AssistantViewClass>
    val interfaces: List<AssistantViewClass>
    val containingFile: VirtualFile?
    val name: String
}
