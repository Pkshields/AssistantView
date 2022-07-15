package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

interface SourceFileInterpreter {
    fun parseFile(psiFile: PsiFile, project: Project): AssistantViewFile?
}
