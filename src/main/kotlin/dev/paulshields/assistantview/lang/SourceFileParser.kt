package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

interface SourceFileParser {
    fun parseFile(psiFile: PsiFile, project: Project): AssistantViewFile?
}
