package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiType
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.Test

class AssistantViewTypeTest {
    private val nameOfPsiType = "TypeName"
    private val psiType = mock<PsiType>().apply {
        every { presentableText } returns nameOfPsiType
    }

    @Test
    fun `test should contain name of the type`() {
        val target = AssistantViewType(psiType)
        val result = target.name

        assertThat(result, equalTo(nameOfPsiType))
    }
}
