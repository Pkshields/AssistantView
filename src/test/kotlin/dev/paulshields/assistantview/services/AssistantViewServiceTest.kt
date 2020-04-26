package dev.paulshields.assistantview.services

import dev.paulshields.assistantview.AssistantView
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.verify
import org.junit.Test

class AssistantViewServiceTest {
    private val target = AssistantViewService()

    private val assistantView = mock<AssistantView>()

    @Test
    fun `test should open file in assistant view`() {
        val file = mock<AssistantViewFile>()
        target.registerAssistantView(assistantView)

        target.openFile(file)

        verify { assistantView.openFile(file) }
    }
}
