package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiClass

class AssistantViewClass(psiClass: PsiClass) {
    val name = psiClass.name.orEmpty()

    override fun toString() = name
}
