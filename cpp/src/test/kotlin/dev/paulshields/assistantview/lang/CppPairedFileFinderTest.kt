package dev.paulshields.assistantview.lang

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.lang.psi.impl.OCFileImpl
import com.jetbrains.cidr.lang.symbols.cpp.OCFunctionSymbol
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol
import dev.paulshields.assistantview.extensions.classes
import dev.paulshields.assistantview.extensions.topLevelFunctions
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test

class CppPairedFileFinderTest {
    private val project = mock<Project>()
    private val filePairedToSourceFile = mock<OCFileImpl>()
    private val sourceFile = mock<CppAssistantViewFile>().apply {
        every { project } returns this@CppPairedFileFinderTest.project
    }
    private val method = mock<OCFunctionSymbol>().apply {
        every { getAssociatedSymbol(project)?.getContainingOCFile(project) } returns filePairedToSourceFile
    }
    private val sourceClass = mock<OCStructSymbol>().apply {
        every { membersList } returns listOf(method)
    }

    private val target = CppPairedFileFinder()

    @Test
    fun `should find paired file from file containing class information`() {
        mockkStatic(OCFileImpl::classes)
        every { sourceFile.ocFile.classes } returns listOf(sourceClass)

        val result = target.findPairedFile(sourceFile)

        assertThat(result?.psiFile).isEqualTo(filePairedToSourceFile)
    }

    @Test
    fun `should find paired file from file containing top level function`() {
        mockkStatic(OCFileImpl::topLevelFunctions)
        every { sourceFile.ocFile.topLevelFunctions } returns listOf(method)

        val result = target.findPairedFile(sourceFile)

        assertThat(result?.psiFile).isEqualTo(filePairedToSourceFile)
    }

    @Test
    fun `should return null if function does not have a predeclaration in another file`() {
        mockkStatic(OCFileImpl::topLevelFunctions)
        every { method.getAssociatedSymbol(project) } returns null
        every { sourceFile.ocFile.topLevelFunctions } returns listOf(method)

        val result = target.findPairedFile(sourceFile)

        assertThat(result?.psiFile).isNull()
    }

    @Test
    fun `should return null if file contains no classes or top level functions`() {
        assertThat(target.findPairedFile(sourceFile)).isNull()
    }

    @Test
    fun `should return null if file contains is not a c++ file`() {
        val notCppFile = mock<AssistantViewFile>()

        assertThat(target.findPairedFile(notCppFile)).isNull()
    }
}
