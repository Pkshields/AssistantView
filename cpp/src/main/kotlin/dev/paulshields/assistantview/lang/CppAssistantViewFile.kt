package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.lang.psi.impl.OCFileImpl
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol

class CppAssistantViewFile(private val ocFile: OCFileImpl, project: Project) : AssistantViewFile(ocFile, project) {
    override fun getClassesFromFile() =
        ocFile.symbolTable
            ?.contents
            ?.filterIsInstance<OCStructSymbol>()
            ?.map { CppAssistantViewClass(it, project) }
            ?: emptyList()
}
