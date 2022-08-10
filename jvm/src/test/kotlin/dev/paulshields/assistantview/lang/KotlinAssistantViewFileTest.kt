package dev.paulshields.assistantview.lang

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.Test

class KotlinAssistantViewFileTest {
    private val name = "ClassName"
    private val extension = "kt"
    private val fileName = "$name.$extension"
    private val mainClassName = "MainClassName"
    private val project = mock<Project>()
    private val mainClass = mock<PsiClass>().apply {
        every { name } returns mainClassName
    }
    private val ktFile = mock<KtFile>().apply {
        every { name } returns fileName
        every { classes } returns arrayOf(mainClass, mainClass)
    }

    private val target = KotlinAssistantViewFile(ktFile, project)

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
        val sameFile = KotlinAssistantViewFile(ktFile, project)

        assertThat(target == sameFile).isTrue()
    }

    @Test
    fun `should correctly fail to compare two different assistant view files`() {
        val differentRawFile = mock<KtFile>()
        val differentFile = KotlinAssistantViewFile(differentRawFile, project)

        assertThat(target == differentFile).isFalse()
    }
}
