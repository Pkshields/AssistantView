package dev.paulshields.assistantview.lang

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import dev.paulshields.assistantview.extensions.extendsClasses
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test

class JvmAssistantViewClassTest {
    private val className = "ClassName"
    private val superClassName = "SuperClass"
    private val superClass = psiClassWithName(superClassName)
    private val interfaces = arrayOf(psiClassWithName(""), psiClassWithName(""), psiClassWithName(""))
    private val containingFile = mock<VirtualFile>()
    private val psiClass = psiClassWithName(className, containingFile)
    private val project = mock<Project>()

    private val target = JvmAssistantViewClass(psiClass, project)

    @Test
    fun `should get super classes`() {
        mockkStatic(PsiClass::extendsClasses)
        every { psiClass.extendsClasses } returns listOf(superClass)

        assertThat(target.superClasses).hasSize(1)
        assertThat(target.superClasses.first().name).isEqualTo(superClassName)
    }

    @Test
    fun `should return null if class has no super class`() {
        mockkStatic(PsiClass::extendsClasses)
        every { psiClass.extendsClasses } returns emptyList()

        assertThat(target.superClasses).isEmpty()
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
    fun `should get containing file`() {
        assertThat(target.containingFile).isEqualTo(containingFile)
    }

    @Test
    fun `should get class name`() {
        assertThat(target.name).isEqualTo(className)
    }

    @Test
    fun `should print name when calling tostring`() {
        assertThat(target.toString()).isEqualTo(className)
    }

    private fun psiClassWithName(psiClassName: String, file: VirtualFile? = null): PsiClass {
        val mock = mock<PsiClass>().apply {
            every { name } returns psiClassName
        }

        file?.let {
            every { mock.containingFile.virtualFile } returns file
        }

        return mock
    }
}
