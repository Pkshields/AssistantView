package dev.paulshields.assistantview.extensions

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import com.jetbrains.cidr.lang.psi.impl.OCFileImpl
import com.jetbrains.cidr.lang.symbols.OCSymbol
import com.jetbrains.cidr.lang.symbols.cpp.OCFunctionSymbol
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class OCFileImplExtensionsTest {
    private val mockClasses = listOf(mock<OCStructSymbol>(), mock())
    private val mockTopLevelFunctions = listOf(mock<OCFunctionSymbol>(), mock())
    private val mockOtherSymbols = listOf(mock<OCSymbol>(), mock())

    private val target = mock<OCFileImpl>().apply {
        every { symbolTable?.contents } returns mockClasses + mockTopLevelFunctions + mockOtherSymbols
    }

    @Test
    fun `should get classes within ocfile`() {
        assertThat(target.classes).containsExactly(*mockClasses.toTypedArray())
    }

    @Test
    fun `should return an empty list if there are no classes within ocfile`() {
        every { target.symbolTable?.contents } returns mockTopLevelFunctions + mockOtherSymbols

        assertThat(target.classes).isEmpty()
    }

    @Test
    fun `should get top level functions within ocfile`() {
        assertThat(target.topLevelFunctions).containsExactly(*mockTopLevelFunctions.toTypedArray())
    }

    @Test
    fun `should return an empty list if there are no top level functions within ocfile`() {
        every { target.symbolTable?.contents } returns mockClasses + mockOtherSymbols

        assertThat(target.topLevelFunctions).isEmpty()
    }
}
