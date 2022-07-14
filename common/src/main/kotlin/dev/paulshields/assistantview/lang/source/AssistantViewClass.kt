package dev.paulshields.assistantview.lang.source

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

interface AssistantViewClass {
    val project: Project
    val superClass: AssistantViewClass?
    val interfaces: List<AssistantViewClass>
    val containingFile: VirtualFile
    val name: String
}
