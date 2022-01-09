package dev.paulshields.assistantview.sourcefiles

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import dev.paulshields.assistantview.extensions.extendsClass

class AssistantViewClass(val psiClass: PsiClass, val project: Project) {
    val superClass by lazy {
        psiClass.extendsClass?.let { AssistantViewClass(it, project) }
    }

    val interfaces by lazy {
        psiClass.interfaces.map { AssistantViewClass(it, project) }
    }

    val name = psiClass.name.orEmpty()

    override fun toString() = name
}
