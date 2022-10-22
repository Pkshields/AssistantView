package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import dev.paulshields.assistantview.extensions.lastIndexOfOrNull

abstract class AssistantViewFile(val psiFile: PsiFile, val project: Project) {
    val classes: List<AssistantViewClass>
        get() = getClassesFromFile()

    val mainClass: AssistantViewClass?
        get() = classes.firstOrNull()

    val fileName: String
        get() = psiFile.name

    val name: String
        get() = fileName.substring(0, fileName.lastIndexOfOrNull('.') ?: fileName.length)

    val extension: String
        get() = fileName.substring(fileName.lastIndexOfOrNull('.')?.plus(1) ?: fileName.length, fileName.length)

    protected abstract fun getClassesFromFile(): List<AssistantViewClass>

    override fun toString() = fileName

    override fun equals(other: Any?) = other is AssistantViewFile && this.psiFile == other.psiFile

    override fun hashCode() = psiFile.hashCode()
}
