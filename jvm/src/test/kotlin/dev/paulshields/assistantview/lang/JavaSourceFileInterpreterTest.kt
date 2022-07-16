package dev.paulshields.assistantview.lang

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import dev.paulshields.assistantview.testcommon.mock
import org.junit.jupiter.api.Test

class JavaSourceFileInterpreterTest {
    private val project = mock<Project>()

    private val target = JavaSourceFileInterpreter()

    @Test
    fun `should parse java file`() {
        val javaFile = mock<PsiJavaFile>()

        val result = target.parseFile(javaFile, project)

        assertThat(result).isNotNull()
        assertThat(result?.psiFile).isEqualTo(javaFile)
        assertThat(result?.project).isEqualTo(project)
    }

    @Test
    fun `should gracefully fail to parse unsupported file`() {
        val unsupportedFile = mock<PsiFile>()

        val result = target.parseFile(unsupportedFile, project)

        assertThat(result).isNull()
    }
}
