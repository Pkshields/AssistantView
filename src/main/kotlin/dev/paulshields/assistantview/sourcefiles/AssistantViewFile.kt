package dev.paulshields.assistantview.sourcefiles

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

abstract class AssistantViewFile(val psiFile: PsiFile, val project: Project) {
    val classes by lazy {
        getClassesFromFile()
    }

    val mainClass by lazy { classes.firstOrNull() }

    val name = psiFile.name

    protected abstract fun getClassesFromFile(): List<AssistantViewClass>

    override fun toString() = name
}
