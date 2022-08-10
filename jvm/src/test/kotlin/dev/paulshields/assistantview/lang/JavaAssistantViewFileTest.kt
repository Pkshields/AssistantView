package dev.paulshields.assistantview.lang

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiJavaFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class JavaAssistantViewFileTest {
    private val name = "ClassName"
    private val extension = "java"
    private val fileName = "$name.$extension"
    private val mainClassName = "MainClassName"
    private val project = mock<Project>()
    private val mainClass = mock<PsiClass>().apply {
        every { name } returns mainClassName
    }
    private val javaFile = mock<PsiJavaFile>().apply {
        every { name } returns fileName
        every { classes } returns arrayOf(mainClass, mainClass)
    }

    private val target = JavaAssistantViewFile(javaFile, project)

    @Test
    fun `should get file name`() {
        assertThat(target.fileName).isEqualTo(fileName)
    }

    @Test
    fun `should get name`() {
        assertThat(target.name).isEqualTo(name)
    }

    @Test
    fun `should get extension`() {
        assertThat(target.extension).isEqualTo(extension)
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
    fun `should print name when calling tostring`() {
        assertThat(target.toString()).isEqualTo(fileName)
    }

    @Test
    fun `should correctly compare two assistant view files with the same underlying files`() {
        val sameFile = JavaAssistantViewFile(javaFile, project)

        assertThat(target == sameFile).isTrue()
    }

    @Test
    fun `should correctly fail to compare two different assistant view files`() {
        val differentRawFile = mock<PsiJavaFile>()
        val differentFile = JavaAssistantViewFile(differentRawFile, project)

        assertThat(target == differentFile).isFalse()
    }
}
