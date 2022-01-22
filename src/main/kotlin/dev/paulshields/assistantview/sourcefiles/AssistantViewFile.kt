package dev.paulshields.assistantview.sourcefiles

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.utils.addToStdlib.lastIndexOfOrNull

abstract class AssistantViewFile(val psiFile: PsiFile, val project: Project) {
    val classes by lazy {
        getClassesFromFile()
    }

    val mainClass by lazy { classes.firstOrNull() }

    val fileName = psiFile.name

    val name by lazy {
        fileName.substring(0, fileName.lastIndexOfOrNull('.') ?: fileName.length)
    }

    protected abstract fun getClassesFromFile(): List<AssistantViewClass>

    override fun toString() = fileName
}
