package dev.paulshields.assistantview.sourcefiles

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.psi.PsiClass
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class AssistantViewClassTest {
    private val className = "ClassName"
    private val psiClass = mock<PsiClass>().apply {
        every { name } returns className
    }

    private val target = AssistantViewClass(psiClass)

    @Test
    fun `should get class name`() {
        assertThat(target.name).isEqualTo(className)
    }

    @Test
    fun `should print name when calling tostring`() {
        assertThat(target.toString()).isEqualTo(className)
    }
}
