package dev.paulshields.assistantview.lang

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol
import dev.paulshields.assistantview.extensions.getSuperClasses
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test

class CppAssistantViewClassTest {
    private val className = "ClassName"
    private val superClassName = "SuperClass"
    private val superClasses = listOf(ocStructSymbolWithName(superClassName))
    private val containingFile = mock<VirtualFile>()
    private val ocStruct = ocStructSymbolWithName(className, containingFile)
    private val project = mock<Project>()

    private val target = CppAssistantViewClass(ocStruct, project)

    @Test
    fun `should get super classes`() {
        mockkStatic(OCStructSymbol::getSuperClasses)
        every { ocStruct.getSuperClasses(project) } returns superClasses

        assertThat(target.superClasses).hasSize(1)
        assertThat(target.superClasses[0].name).isEqualTo(superClassName)
    }

    @Test
    fun `should return an empty list if class has no super class`() {
        mockkStatic(OCStructSymbol::getSuperClasses)
        every { ocStruct.getSuperClasses(project) } returns emptyList()

        assertThat(target.superClasses).isEmpty()
    }

    @Test
    fun `should always return an empty interfaces list`() {
        assertThat(target.interfaces).isEmpty()
    }

    @Test
    fun `should get containing file`() {
        assertThat(target.containingFile).isEqualTo(containingFile)
    }

    @Test
    fun `should return null if class has no containing file`() {
        every { ocStruct.containingFile } returns null

        assertThat(target.containingFile).isNull()
    }

    @Test
    fun `should get class name`() {
        assertThat(target.name).isEqualTo(className)
    }

    @Test
    fun `should print name when calling tostring`() {
        assertThat(target.toString()).isEqualTo(className)
    }

    private fun ocStructSymbolWithName(className: String, file: VirtualFile? = null): OCStructSymbol {
        val mock = mock<OCStructSymbol>().apply {
            every { name } returns className
        }

        file?.let {
            every { mock.containingFile } returns file
        }

        return mock
    }
}
