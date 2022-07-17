package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.cidr.lang.psi.impl.OCFileImpl

class CppSourceFileInterpreter : SourceFileInterpreter {
    override fun parseFile(psiFile: PsiFile, project: Project) =
        if (psiFile is OCFileImpl) CppAssistantViewFile(psiFile, project) else null
}
