package dev.paulshields.assistantview.sourcefiles.files

import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import org.jetbrains.kotlin.psi.KtFile

class KotlinAssistantViewFile(private val ktFile: KtFile, project: Project) : AssistantViewFile(ktFile, project) {
    override fun getClassesFromFile() = ktFile.classes.map { clazz -> AssistantViewClass(clazz, project) }.toList()
}
