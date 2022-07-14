package dev.paulshields.assistantview.lang.parser

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import dev.paulshields.assistantview.lang.source.AssistantViewFile

interface SourceFileParser {
    fun parseFile(psiFile: PsiFile, project: Project): AssistantViewFile?
}
