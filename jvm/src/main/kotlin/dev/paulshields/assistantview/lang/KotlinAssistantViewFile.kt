package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.psi.KtFile

class KotlinAssistantViewFile(private val ktFile: KtFile, project: Project) : AssistantViewFile(ktFile, project) {
    override fun getClassesFromFile() = ktFile.classes.map { clazz -> JvmAssistantViewClass(clazz, project) }.toList()
}
