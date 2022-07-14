package dev.paulshields.assistantview.extensions

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiJavaCodeReferenceElement
import com.intellij.psi.PsiReferenceList
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class PsiClassExtensionsTest {
    private val baseClass = mock<PsiClass>()
    private val baseClasses = listOf(baseClass, mock())

    private val psiClassWithBaseClasses = psiClassWithBaseClasses(baseClasses)
    private val psiClassWithoutBaseClasses = psiClassWithBaseClasses(emptyList())

    @Test
    fun `should return all base classes`() {
        assertThat(psiClassWithBaseClasses.extendsClasses).containsExactly(*baseClasses.toTypedArray())
    }

    @Test
    fun `should return empty list if no base classes`() {
        assertThat(psiClassWithoutBaseClasses.extendsClasses).isEmpty()
    }

    @Test
    fun `should return first base classes as the main base class`() {
        assertThat(psiClassWithBaseClasses.extendsClass).isEqualTo(baseClass)
    }

    @Test
    fun `should return null is no main base class`() {
        assertThat(psiClassWithoutBaseClasses.extendsClass).isNull()
    }

    private fun psiClassWithBaseClasses(baseClasses: List<PsiClass>): PsiClass {
        val psiClass = mock<PsiClass>()

        val baseClassElements = baseClasses.map {
            mock<PsiJavaCodeReferenceElement>().apply {
                every { resolve() } returns it
            }
        }.toTypedArray()

        val referenceList = mock<PsiReferenceList>().apply {
            every { referenceElements } returns baseClassElements
        }

        every { psiClass.extendsList } returns referenceList

        return psiClass
    }
}
