package dev.paulshields.assistantview.sourcefiles

import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.apache.commons.lang.NotImplementedException
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.junit.Test

class AssistantViewFileTest {
    private val ktFile = mock<KtFile>().apply {
        every { classes } returns emptyArray()
    }

    @Test
    fun `test should return class list from kotlin file`() {
        val psiClass = mock<PsiClass>()
        every { ktFile.classes } returns arrayOf(psiClass)

        val target = AssistantViewFile(ktFile)
        val result = target.classes

        assertThat(result.size, equalTo(1))
        assertThat(result[0].underlyingPsiClass, equalTo(psiClass))
    }

    @Test
    fun `test should handle empty class list on kotlin file`() {
        val target = AssistantViewFile(ktFile)
        val result = target.classes

        assertThat(result.size, equalTo(0))
    }

    @Test(expected = NotImplementedException::class)
    fun `test should throw exception when getting class list if psifile is not supported`() {
        val unsupportedPsiFile = mock<PsiFile>()

        val target = AssistantViewFile(unsupportedPsiFile)
        val result = target.classes
    }

    @Test
    fun `test should define first class in class list as main class`() {
        val psiClass = mock<PsiClass>()
        every { ktFile.classes } returns arrayOf(psiClass)

        val target = AssistantViewFile(ktFile)
        val result = target.mainClass

        assertThat(result?.underlyingPsiClass, equalTo(psiClass))
    }

    @Test
    fun `test should handle empty class list when defining main class`() {
        val target = AssistantViewFile(ktFile)
        val result = target.mainClass

        assertThat(result, absent())
    }

    @Test
    fun `test should provide file type`() {
        val fileType: FileType = KotlinFileType.INSTANCE
        every { ktFile.fileType } returns fileType
        val target = AssistantViewFile(ktFile)

        val result = target.fileType

        assertThat(result, equalTo(fileType))
    }
}
