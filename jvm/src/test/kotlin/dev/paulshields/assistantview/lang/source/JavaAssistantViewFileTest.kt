package dev.paulshields.assistantview.lang.source

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiJavaFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class JavaAssistantViewFileTest {
    private val name = "ClassName"
    private val fileName = "$name.kt"
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
}
