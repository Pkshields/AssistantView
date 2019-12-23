package dev.paulshields.assistantview.sourcefiles

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import org.apache.commons.lang.NotImplementedException
import org.jetbrains.kotlin.psi.KtFile

class AssistantViewFile private constructor(public val psiFile: PsiFile) {
    companion object {
        private const val LANGUAGE_IS_NOT_SUPPORTED_ERROR = "Language is not yet supported."

        fun fromVirtualFile(virtualFile: VirtualFile, project: Project) = project
            .getService(PsiManager::class.java)
            .findFile(virtualFile)
            ?.let {
                AssistantViewFile(it)
            }
    }

    val classes = when (psiFile) {
        is KtFile -> psiFile.classes.map(::AssistantViewClass).toList()
        is PsiJavaFile -> throw NotImplementedException("Java support is coming soon")
        else -> throw NotImplementedException(LANGUAGE_IS_NOT_SUPPORTED_ERROR)
    }

    val mainClass = if (classes.count() > 0) classes[0] else null
}
