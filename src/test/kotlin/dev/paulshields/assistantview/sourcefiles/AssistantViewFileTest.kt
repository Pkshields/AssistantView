package dev.paulshields.assistantview.sourcefiles

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

abstract class AssistantViewFileTest {
    protected val fileName = "ClassName"

    abstract val target: AssistantViewFile

    @Test
    fun `should get name`() {
        assertThat(target.name).isEqualTo(fileName)
    }

    @Test
    fun `should get classes`() {
        assertThat(target.classes).hasSize(2)
    }

    @Test
    fun `should print name when calling tostring`() {
        assertThat(target.toString()).isEqualTo(fileName)
    }
}
