package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiClass

class AssistantViewClass(val psiClass: PsiClass) {
    val baseClass = psiClass.extendsList
        ?.referenceElements
        ?.mapNotNull { it.resolve() as? PsiClass }
        ?.map(::AssistantViewClass)
        ?.firstOrNull()

    val interfaces = psiClass.implementsList
        ?.referenceElements
        ?.mapNotNull { it.resolve() as? PsiClass }
        ?.map(::AssistantViewClass) ?: emptyList()
}
