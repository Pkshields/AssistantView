package dev.paulshields.assistantview.extensions

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol
import com.jetbrains.cidr.lang.types.OCReferenceTypeSimple
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class OCStructSymbolExtensionsTest {
    private val fileContainingClass = mock<PsiFile>()
    private val project = mock<Project>()
    private val superClass = mock<OCStructSymbol>()

    private val target = mock<OCStructSymbol>().apply {
        every { getContainingPsiFile(project) } returns fileContainingClass
        every { getBaseCppClasses(fileContainingClass) } returns listOf(referenceContainingClass(superClass))
    }

    @Test
    fun `should get superclasses`() {
        val result = target.getSuperClasses(project)

        assertThat(result).containsExactly(superClass)
    }

    @Test
    fun `should handle if class is not within a file when getting superclasses`() {
        every { target.getContainingPsiFile(project) } returns null

        val result = target.getSuperClasses(project)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should handle if class does not have any superclasses`() {
        every { target.getBaseCppClasses(fileContainingClass) } returns emptyList()

        val result = target.getSuperClasses(project)

        assertThat(result).isEmpty()
    }

    private fun referenceContainingClass(clazz: OCStructSymbol) =
        mock<OCReferenceTypeSimple>().apply {
            every { reference.resolveToSymbols(any()) } returns listOf(clazz)
        }
}
