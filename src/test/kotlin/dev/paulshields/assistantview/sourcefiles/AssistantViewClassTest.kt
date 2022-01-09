package dev.paulshields.assistantview.sourcefiles

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import dev.paulshields.assistantview.extensions.extendsClass
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test

class AssistantViewClassTest {
    private val className = "ClassName"
    private val superClass = psiClassWithName("")
    private val interfaces = arrayOf(psiClassWithName(""), psiClassWithName(""), psiClassWithName(""))
    private val psiClass = psiClassWithName(className)
    private val project = mock<Project>()

    private val target = AssistantViewClass(psiClass, project)

    @Test
    fun `should get super class`() {
        mockkStatic(PsiClass::extendsClass)
        every { psiClass.extendsClass } returns superClass

        assertThat(target.superClass?.psiClass).isEqualTo(superClass)
    }

    @Test
    fun `should return null if class has no super class`() {
        mockkStatic(PsiClass::extendsClass)
        every { psiClass.extendsClass } returns null

        assertThat(target.superClass).isNull()
    }

    @Test
    fun `should get interfaces`() {
        every { psiClass.interfaces } returns interfaces

        assertThat(target.interfaces).hasSize(3)
    }

    @Test
    fun `should return empty list if class implements no interfaces`() {
        every { psiClass.interfaces } returns emptyArray()

        assertThat(target.interfaces).isEmpty()
    }

    @Test
    fun `should get class name`() {
        assertThat(target.name).isEqualTo(className)
    }

    @Test
    fun `should print name when calling tostring`() {
        assertThat(target.toString()).isEqualTo(className)
    }

    private fun psiClassWithName(psiClassName: String) =
        mock<PsiClass>().apply {
            every { name } returns psiClassName
        }
}
