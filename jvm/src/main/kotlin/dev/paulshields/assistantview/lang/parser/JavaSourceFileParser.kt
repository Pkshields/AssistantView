package dev.paulshields.assistantview.lang.parser

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import dev.paulshields.assistantview.lang.source.JavaAssistantViewFile

class JavaSourceFileParser : SourceFileParser {
    override fun parseFile(psiFile: PsiFile, project: Project) =
        if (psiFile is PsiJavaFile) JavaAssistantViewFile(psiFile, project) else null
}