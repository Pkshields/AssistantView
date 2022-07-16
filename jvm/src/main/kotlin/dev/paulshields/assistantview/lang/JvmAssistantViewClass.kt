package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import dev.paulshields.assistantview.extensions.extendsClasses

class JvmAssistantViewClass(private val psiClass: PsiClass, override val project: Project) : AssistantViewClass {
    override val superClasses by lazy {
        psiClass.extendsClasses.map { JvmAssistantViewClass(it, project) }
    }

    override val interfaces by lazy {
        psiClass.interfaces.map { JvmAssistantViewClass(it, project) }
    }

    override val containingFile by lazy {
        psiClass.containingFile.virtualFile
    }

    override val name = psiClass.name.orEmpty()

    override fun toString() = name
}
