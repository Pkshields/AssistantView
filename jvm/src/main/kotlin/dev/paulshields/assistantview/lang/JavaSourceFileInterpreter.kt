package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile

class JavaSourceFileInterpreter : SourceFileInterpreter {
    override fun parseFile(psiFile: PsiFile, project: Project) =
        if (psiFile is PsiJavaFile) JavaAssistantViewFile(psiFile, project) else null
}
