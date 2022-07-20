package dev.paulshields.assistantview.lang

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.lang.psi.impl.OCFileImpl
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol
import dev.paulshields.assistantview.extensions.classes
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test

class CppAssistantViewFileTest {
    private val name = "ClassName"
    private val fileName = "$name.kt"
    private val mainClassName = "MainClassName"
    private val project = mock<Project>()
    private val mainClass = mock<OCStructSymbol>().apply {
        every { name } returns mainClassName
    }
    private val file = mock<OCFileImpl>().apply {
        mockkStatic(OCFileImpl::classes)
        every { name } returns fileName
        every { classes } returns listOf(mainClass, mainClass)
    }

    private val target = CppAssistantViewFile(file, project)

    @Test
    fun `should get file name`() {
        assertThat(target.fileName).isEqualTo(fileName)
    }

    @Test
    fun `should get name`() {
        assertThat(target.name).isEqualTo(name)
    }

    @Test
    fun `should get classes`() {
        assertThat(target.classes).hasSize(2)
    }

    @Test
    fun `should get main class`() {
        assertThat(target.mainClass?.name).isEqualTo(mainClassName)
    }

    @Test
    fun `should return null if file contains no classes`() {
        every { file.classes } returns emptyList()

        assertThat(target.mainClass).isNull()
    }

    @Test
    fun `should print name when calling tostring`() {
        assertThat(target.toString()).isEqualTo(fileName)
    }
}
