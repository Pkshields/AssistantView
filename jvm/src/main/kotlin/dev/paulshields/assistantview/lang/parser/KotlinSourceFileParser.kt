package dev.paulshields.assistantview.lang.parser

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import dev.paulshields.assistantview.lang.source.KotlinAssistantViewFile
import org.jetbrains.kotlin.psi.KtFile

class KotlinSourceFileParser : SourceFileParser {
    override fun parseFile(psiFile: PsiFile, project: Project) =
        if (psiFile is KtFile) KotlinAssistantViewFile(psiFile, project) else null
}
