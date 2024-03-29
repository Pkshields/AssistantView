package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol
import dev.paulshields.assistantview.extensions.getSuperClasses

class CppAssistantViewClass(private val classSymbol: OCStructSymbol, override val project: Project) : AssistantViewClass {
    override val superClasses: List<AssistantViewClass>
        get() = classSymbol
            .getSuperClasses(project)
            .map { CppAssistantViewClass(it, project) }

    override val interfaces = emptyList<AssistantViewClass>()

    override val containingFile: VirtualFile?
        get() = classSymbol.containingFile

    override val name = classSymbol.name

    override fun toString() = name
}
