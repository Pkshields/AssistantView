package dev.paulshields.assistantview.extensions

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class ProjectExtensionsTest {
    private val dumbService = mock<DumbService>()

    private val target = mock<Project>().apply {
        every { getService(DumbService::class.java) } returns dumbService
    }

    @Test
    fun `should return true if project is in dumb mode`() {
        every { dumbService.isDumb } returns true

        assertThat(target.isDumb).isTrue()
    }

    @Test
    fun `should return false if project is not in dumb mode`() {
        every { dumbService.isDumb } returns false

        assertThat(target.isDumb).isFalse()
    }
}
