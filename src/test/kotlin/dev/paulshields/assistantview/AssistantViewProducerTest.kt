package dev.paulshields.assistantview

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import dev.paulshields.assistantview.factories.AssistantViewFactory
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileOpenerService
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.mockKoinApplication
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module

class AssistantViewProducerTest {
    private val toolWindow = mock<ToolWindow>()
    private val assistantView = mock<AssistantView>()
    private val project = mock<Project>()

    private val assistantViewService = mock<AssistantViewService>()
    private val fileOpenerService = mock<FileOpenerService>()
    private val assistantViewFactory = mock<AssistantViewFactory>().apply {
        every { createAssistantView(toolWindow, project) } returns assistantView
    }

    @JvmField
    @RegisterExtension
    val dependencyInjector = mockKoinApplication(
        module {
            single { assistantViewService }
            single { fileOpenerService }
            single { assistantViewFactory }
        }
    )

    private val target = AssistantViewProducer()

    @Test
    fun `should create assistant view for project`() {
        target.createToolWindowContent(project, toolWindow)

        verify { assistantViewFactory.createAssistantView(toolWindow, project) }
    }

    @Test
    fun `should register new assistant view`() {
        target.createToolWindowContent(project, toolWindow)

        verify { assistantViewService.registerAssistantViewForProject(assistantView, project) }
    }

    @Test
    fun `should attempt to open counterpart for tab currently in focus`() {
        target.createToolWindowContent(project, toolWindow)

        verify { fileOpenerService.openCounterpartForTabInFocus(project) }
    }

    @Test
    fun `should close assistant view when project is closed`() {
        target.createToolWindowContent(project, toolWindow)

        Disposer.dispose(project)

        verify { assistantViewService.closeAssistantViewForProject(project) }
    }
}
