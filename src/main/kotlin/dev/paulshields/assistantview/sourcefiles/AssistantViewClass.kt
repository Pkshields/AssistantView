package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiClass

class AssistantViewClass(val underlyingPsiClass: PsiClass) {
    val baseClass by lazy {
        underlyingPsiClass.extendsList
            ?.referenceElements
            ?.mapNotNull { it.resolve() as? PsiClass }
            ?.map(::AssistantViewClass)
            ?.firstOrNull()
    }

    val interfaces by lazy {
        underlyingPsiClass.implementsList
            ?.referenceElements
            ?.mapNotNull { it.resolve() as? PsiClass }
            ?.map(::AssistantViewClass) ?: emptyList()
    }

    val properties by lazy {
        underlyingPsiClass.fields
            .map(::AssistantViewProperty)
    }

    val name = underlyingPsiClass.name.orEmpty()

    override fun toString() = name
}
