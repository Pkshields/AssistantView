package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import dev.paulshields.assistantview.extensions.extendsClass

class JvmAssistantViewClass(private val psiClass: PsiClass, override val project: Project) : AssistantViewClass {
    override val superClass by lazy {
        psiClass.extendsClass?.let { JvmAssistantViewClass(it, project) }
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
