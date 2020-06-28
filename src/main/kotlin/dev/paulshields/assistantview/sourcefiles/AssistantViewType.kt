package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiType

class AssistantViewType(val underlyingPsiType: PsiType) {
    val name = underlyingPsiType.presentableText

    override fun toString() = name
}
