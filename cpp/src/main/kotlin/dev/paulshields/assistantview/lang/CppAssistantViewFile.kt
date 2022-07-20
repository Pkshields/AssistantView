package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.lang.psi.impl.OCFileImpl
import dev.paulshields.assistantview.extensions.classes

class CppAssistantViewFile(val ocFile: OCFileImpl, project: Project) : AssistantViewFile(ocFile, project) {
    override fun getClassesFromFile() = ocFile.classes.map { CppAssistantViewClass(it, project) }
}
