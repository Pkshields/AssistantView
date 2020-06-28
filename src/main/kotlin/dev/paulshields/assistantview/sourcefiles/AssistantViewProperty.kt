package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiField

class AssistantViewProperty(underlyingField: PsiField) {
    val typeClass by lazy { AssistantViewType(underlyingField.type) }

    val name = underlyingField.name

    override fun toString() = name
}
