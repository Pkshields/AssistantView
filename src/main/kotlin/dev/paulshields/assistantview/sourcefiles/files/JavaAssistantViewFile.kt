package dev.paulshields.assistantview.sourcefiles.files

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiJavaFile
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class JavaAssistantViewFile(private val javaFile: PsiJavaFile, project: Project) : AssistantViewFile(javaFile, project) {
    override fun getClassesFromFile() = javaFile.classes.map { clazz -> AssistantViewClass(clazz, project) }.toList()
}
