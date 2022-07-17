package dev.paulshields.assistantview.lang

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.cidr.lang.psi.impl.OCFileImpl
import dev.paulshields.assistantview.testcommon.mock
import org.junit.jupiter.api.Test

class CppSourceFileInterpreterTest {
    private val project = mock<Project>()

    private val target = CppSourceFileInterpreter()

    @Test
    fun `should parse c++ file`() {
        val cppFile = mock<OCFileImpl>()

        val result = target.parseFile(cppFile, project)

        assertThat(result).isNotNull()
        assertThat(result?.psiFile).isEqualTo(cppFile)
        assertThat(result?.project).isEqualTo(project)
    }

    @Test
    fun `should gracefully fail to parse unsupported file`() {
        val unsupportedFile = mock<PsiFile>()

        val result = target.parseFile(unsupportedFile, project)

        assertThat(result).isNull()
    }
}
