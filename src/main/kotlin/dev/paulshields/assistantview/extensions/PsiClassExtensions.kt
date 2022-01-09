package dev.paulshields.assistantview.extensions

import com.intellij.psi.PsiClass

val PsiClass.extendsClasses: List<PsiClass>
    get() = this.extendsList
        ?.referenceElements
        ?.mapNotNull { it.resolve() as? PsiClass } ?: emptyList()

val PsiClass.extendsClass: PsiClass?
    get() = this.extendsClasses.firstOrNull()
