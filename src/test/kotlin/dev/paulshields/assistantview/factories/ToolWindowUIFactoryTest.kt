package dev.paulshields.assistantview.factories

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.openapi.editor.Editor
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import dev.paulshields.assistantview.factories.intellij.IntellijSingletons
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.ui.StartupAssistantView
import io.mockk.every
import org.junit.jupiter.api.Test
import javax.swing.JComponent

class ToolWindowUIFactoryTest {
    private val expectedContent = mock<Content>()
    private val contentFactory = mock<ContentFactory>()
    private val intellijSingletons = mock<IntellijSingletons>().apply {
        every { contentFactory } returns this@ToolWindowUIFactoryTest.contentFactory
    }

    private val target = ToolWindowUIFactory(intellijSingletons)

    @Test
    fun `should create tool window content from code editor`() {
        val jComponent = mock<JComponent>()
        val editor = mock<Editor>().apply {
            every { component } returns jComponent
        }
        every { contentFactory.createContent(jComponent, any(), any()) } returns expectedContent

        val result = target.createContentForCodeEditor(editor)

        assertThat(result).isEqualTo(expectedContent)
    }

    @Test
    fun `should create startup assistant view content`() {
        every { contentFactory.createContent(StartupAssistantView.panel, any(), any()) } returns expectedContent

        val result = target.createStartupAssistantViewContent()

        assertThat(result).isEqualTo(expectedContent)
    }
}
