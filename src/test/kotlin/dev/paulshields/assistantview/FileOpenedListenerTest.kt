package dev.paulshields.assistantview

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import dev.paulshields.assistantview.services.AssistantViewEventService
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.mockKoinApplication
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest

class FileOpenedListenerTest : KoinTest {
    private val assistantViewEventService = mock<AssistantViewEventService>()

    @JvmField
    @RegisterExtension
    val dependencyInjector = mockKoinApplication(
        module {
            single { assistantViewEventService }
        }
    )

    private val target = FileOpenedListener()

    @Test
    fun `should send file opened event to the assistant view event service`() {
        val fileEditorManagerEvent = mock<FileEditorManagerEvent>()

        target.selectionChanged(fileEditorManagerEvent)

        verify(exactly = 1) { assistantViewEventService.handleFileOpenedEvent(fileEditorManagerEvent) }
    }
}
