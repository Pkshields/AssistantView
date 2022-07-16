package dev.paulshields.assistantview.lang

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import dev.paulshields.assistantview.testcommon.mock
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.Test

class KotlinSourceFileInterpreterTest {
    private val project = mock<Project>()

    private val target = KotlinSourceFileInterpreter()

    @Test
    fun `should parse kotlin file`() {
        val kotlinFile = mock<KtFile>()

        val result = target.parseFile(kotlinFile, project)

        assertThat(result).isNotNull()
        assertThat(result?.psiFile).isEqualTo(kotlinFile)
        assertThat(result?.project).isEqualTo(project)
    }

    @Test
    fun `should gracefully fail to parse unsupported file`() {
        val unsupportedFile = mock<PsiFile>()

        val result = target.parseFile(unsupportedFile, project)

        assertThat(result).isNull()
    }
}
