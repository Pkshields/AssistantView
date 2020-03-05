package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiClass

class AssistantViewClass(val underlyingPsiClass: PsiClass) {

    val baseClass = underlyingPsiClass.extendsList
        ?.referenceElements
        ?.mapNotNull { it.resolve() as? PsiClass }
        ?.map(::AssistantViewClass)
        ?.firstOrNull()

    val interfaces = underlyingPsiClass.implementsList
        ?.referenceElements
        ?.mapNotNull { it.resolve() as? PsiClass }
        ?.map(::AssistantViewClass) ?: emptyList()

    override fun toString() = underlyingPsiClass.name.orEmpty()
}
