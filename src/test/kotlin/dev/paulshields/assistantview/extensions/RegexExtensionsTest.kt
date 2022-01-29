package dev.paulshields.assistantview.extensions

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import org.junit.jupiter.api.Test

class RegexExtensionsTest {
    private val target = "(Mario)(Luigi)Bowser".toRegex()

    @Test
    fun `should extract groups from regex`() {
        val result = target.extractGroups("MarioLuigiBowser")

        assertThat(result).containsExactly("Mario", "Luigi")
    }

    @Test
    fun `should return empty ist of regex does not match`() {
        val result = target.extractGroups("InvalidString")

        assertThat(result).isEmpty()
    }
}
