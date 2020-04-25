package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import org.apache.commons.lang.NotImplementedException
import org.jetbrains.kotlin.psi.KtFile

class AssistantViewFile constructor(val underlyingPsiFile: PsiFile) {
    companion object {
        private const val LANGUAGE_IS_NOT_SUPPORTED_ERROR = "Language is not yet supported."
    }

    val classes by lazy {
        when (underlyingPsiFile) {
            is KtFile -> underlyingPsiFile.classes.map(::AssistantViewClass).toList()
            is PsiJavaFile -> throw NotImplementedException("Java support is coming soon")
            else -> throw NotImplementedException(LANGUAGE_IS_NOT_SUPPORTED_ERROR)
        }
    }

    val mainClass by lazy {
        if (classes.count() > 0) classes[0] else null
    }

    val fileType by lazy {
        underlyingPsiFile.fileType
    }

    override fun toString() = underlyingPsiFile.name
}
