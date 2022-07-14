package dev.paulshields.assistantview.extensions

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import org.junit.jupiter.api.Test

class StringExtensionsTest {
    private val target = "strings"

    @Test
    fun `should find index of char that exists in string once`() {
        val result = target.lastIndexOfOrNull('r')

        assertThat(result).isEqualTo(2)
    }

    @Test
    fun `should find last index of char that exists in string twice`() {
        val result = target.lastIndexOfOrNull('s')

        assertThat(result).isEqualTo(6)
    }

    @Test
    fun `should return null if char does not exist in string`() {
        val result = target.lastIndexOfOrNull('x')

        assertThat(result).isNull()
    }
}
