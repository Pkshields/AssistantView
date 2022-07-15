package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.KtFile

class KotlinSourceFileInterpreter : SourceFileInterpreter {
    override fun parseFile(psiFile: PsiFile, project: Project) =
        if (psiFile is KtFile) KotlinAssistantViewFile(psiFile, project) else null
}
