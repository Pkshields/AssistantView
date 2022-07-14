package dev.paulshields.assistantview.lang.source

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiJavaFile

class JavaAssistantViewFile(private val javaFile: PsiJavaFile, project: Project) : AssistantViewFile(javaFile, project) {
    override fun getClassesFromFile() = javaFile.classes.map { clazz -> JvmAssistantViewClass(clazz, project) }.toList()
}
