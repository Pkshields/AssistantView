package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiClass

class AssistantViewClass(val psiClass: PsiClass) {
    val superClass by lazy {
        psiClass.superClass?.let(::AssistantViewClass)
    }

    val interfaces by lazy {
        psiClass.interfaces.map(::AssistantViewClass)
    }

    val name = psiClass.name.orEmpty()

    override fun toString() = name
}
