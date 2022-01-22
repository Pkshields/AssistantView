package dev.paulshields.assistantview.sourcefiles

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

abstract class AssistantViewFileTest {
    private val name = "ClassName"
    protected val fileName = "ClassName.kt"
    protected val project = mock<Project>()
    protected val mainClass = mock<PsiClass>().apply {
        every { name } returns "ClassName"
    }

    abstract val target: AssistantViewFile

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
        assertThat(target.mainClass?.psiClass).isEqualTo(mainClass)
    }

    @Test
    fun `should print name when calling tostring`() {
        assertThat(target.toString()).isEqualTo(fileName)
    }
}
