package dev.paulshields.assistantview.intellijwrappers

import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiTypesUtil as IntellijPsiTypesUtil

class PsiTypesUtil {
    fun getPsiClass(psiType: PsiType) = IntellijPsiTypesUtil.getPsiClass(psiType)
}
