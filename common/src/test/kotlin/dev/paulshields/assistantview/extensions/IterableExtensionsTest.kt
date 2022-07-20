package dev.paulshields.assistantview.extensions

import assertk.assertThat
import assertk.assertions.containsExactly
import org.junit.jupiter.api.Test

class IterableExtensionsTest {
    @Test
    fun `should correctly flat map two lists`() {
        val target = listOf(listOf(1, 2, 3), listOf(4, 5, 6))

        val result = target.flatMapNotNull { it }

        assertThat(result).containsExactly(1, 2, 3, 4, 5, 6)
    }

    @Test
    fun `should correctly remove nulls when flat mapping two lists`() {
        val target = listOf(listOf(1, 2, 3), null, listOf(4, 5, 6))

        val result: List<Int> = target.flatMapNotNull { it }

        assertThat(result).containsExactly(1, 2, 3, 4, 5, 6)
    }
}
