package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiField
import com.intellij.psi.PsiType
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.relaxedMock
import io.mockk.every
import org.junit.Test

class AssistantViewPropertyTest {
    private val typeOfPsiProperty = relaxedMock<PsiType>()
    private val nameOfPsiProperty = "PropertyName"
    private val psiProperty = mock<PsiField>().apply {
        every { type } returns typeOfPsiProperty
        every { name } returns nameOfPsiProperty
    }

    @Test
    fun `test should contain property type`() {
        val target = AssistantViewProperty(psiProperty)
        val result = target.typeClass

        assertThat(result.underlyingPsiType, equalTo(typeOfPsiProperty))
    }

    @Test
    fun `test should have the name of the property`() {
        val target = AssistantViewProperty(psiProperty)
        val result = target.name

        assertThat(result, equalTo(psiProperty.name))
    }
}
