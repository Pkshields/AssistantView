package dev.paulshields.assistantview.factories

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import dev.paulshields.assistantview.factories.intellij.IntellijSingletons
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test
import javax.swing.JComponent

class ToolWindowContentFactoryTest {
    private val contentFactory = mock<ContentFactory>()
    private val intellijSingletons = mock<IntellijSingletons>().apply {
        every { contentFactory } returns this@ToolWindowContentFactoryTest.contentFactory
    }

    private val target = ToolWindowContentFactory(intellijSingletons)

    @Test
    fun `should create tool window content from jcomponent`() {
        val jComponent = mock<JComponent>()
        val expectedContent = mock<Content>()
        every { contentFactory.createContent(jComponent, any(), any()) } returns expectedContent

        val result = target.createContent(jComponent)

        assertThat(result).isEqualTo(expectedContent)
    }
}
