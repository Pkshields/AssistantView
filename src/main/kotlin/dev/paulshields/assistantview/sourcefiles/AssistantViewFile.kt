package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiFile

abstract class AssistantViewFile(val psiFile: PsiFile) {
    val classes by lazy {
        getClassesFromFile()
    }

    val name = psiFile.name

    protected abstract fun getClassesFromFile(): List<AssistantViewClass>

    override fun toString() = name
}
