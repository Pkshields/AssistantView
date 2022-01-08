package dev.paulshields.assistantview.sourcefiles.files

import com.intellij.psi.PsiClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFileTest
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.jetbrains.kotlin.psi.KtFile

class KotlinAssistantViewFileTest : AssistantViewFileTest() {
    private val psiClass = mock<PsiClass>().apply {
        every { name } returns "ClassName"
    }

    private val ktFile = mock<KtFile>().apply {
        every { name } returns fileName
        every { classes } returns arrayOf(psiClass, psiClass)
    }

    override val target = KotlinAssistantViewFile(ktFile)
}
