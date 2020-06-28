package dev.paulshields.assistantview.sourcefiles

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiJavaCodeReferenceElement
import com.intellij.psi.PsiReferenceList
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.relaxedMock
import io.mockk.every
import org.junit.Test

class AssistantViewClassTest {
    private val psiClass = relaxedMock<PsiClass>()

    @Test
    fun `test should return one base class`() {
        val baseClass = relaxedMock<PsiClass>()
        givenMockClassExtendsBaseClass(baseClass)

        val target = AssistantViewClass(psiClass)
        val result = target.baseClass

        assertThat(result?.underlyingPsiClass, equalTo(baseClass))
    }

    @Test
    fun `test should return null when class has no base class`() {
        val target = AssistantViewClass(psiClass)
        val result = target.baseClass

        assertThat(result, absent())
    }

    @Test
    fun `test should return first base class is multiple are specified`() {
        val baseClassOne = relaxedMock<PsiClass>()
        val baseClassTwo = relaxedMock<PsiClass>()
        givenMockClassExtendsBaseClass(baseClassOne, baseClassTwo)

        val target = AssistantViewClass(psiClass)
        val result = target.baseClass

        assertThat(result?.underlyingPsiClass, equalTo(baseClassOne))
    }

    @Test
    fun `test should return interfaces`() {
        val interfaceOne = relaxedMock<PsiClass>()
        val interfaceTwo = relaxedMock<PsiClass>()
        givenMockClassImplementsInterfaces(interfaceOne, interfaceTwo)

        val target = AssistantViewClass(psiClass)
        val result = target.interfaces

        assertThat(result.size, equalTo(2))
        assertThat(result[0].underlyingPsiClass, equalTo(interfaceOne))
        assertThat(result[1].underlyingPsiClass, equalTo(interfaceTwo))
    }

    @Test
    fun `test should return empty list when class has no interfaces`() {
        val target = AssistantViewClass(psiClass)
        val result = target.interfaces

        assertThat(result.size, equalTo(0))
    }

    @Test
    fun `test should contain properties`() {
        val property1 = mockPsiField("Name1")
        val property2 = mockPsiField("Name2")
        givenMockClassContainsProperties(property1, property2)

        val target = AssistantViewClass(psiClass)
        val result = target.properties

        assertThat(result.size, equalTo(2))
        assertThat(result[0].name, equalTo(property1.name))
        assertThat(result[1].name, equalTo(property2.name))
    }

    @Test
    fun `test should return empty list when class has no properties`() {
        val target = AssistantViewClass(psiClass)
        val result = target.properties

        assertThat(result.size, equalTo(0))
    }

    @Test
    fun `test should have name`() {
        every { psiClass.name } returns "GoodClassName"

        val target = AssistantViewClass(psiClass)
        val result = target.name

        assertThat(result, equalTo(psiClass.name))
    }

    private fun givenMockClassExtendsBaseClass(vararg psiClasses: PsiClass) {
        val referenceElements = psiClasses.map { psiClass ->
            mock<PsiJavaCodeReferenceElement>().apply {
                every { resolve() } returns psiClass
            }
        }.toTypedArray()

        val extendsList = mock<PsiReferenceList>()
        every { extendsList.referenceElements } returns referenceElements

        every { psiClass.extendsList } returns extendsList
    }

    private fun givenMockClassImplementsInterfaces(vararg psiClasses: PsiClass) {
        val referenceElements = psiClasses.map { psiClass ->
            mock<PsiJavaCodeReferenceElement>().apply {
                every { resolve() } returns psiClass
            }
        }.toTypedArray()

        val implementsList = mock<PsiReferenceList>()
        every { implementsList.referenceElements } returns referenceElements

        every { psiClass.implementsList } returns implementsList
    }

    private fun givenMockClassContainsProperties(vararg psiProperties: PsiField) =
        every { psiClass.fields } returns psiProperties

    private fun mockPsiField(fieldName: String) = mock<PsiField>().apply {
        every { name } returns fieldName
    }
}
