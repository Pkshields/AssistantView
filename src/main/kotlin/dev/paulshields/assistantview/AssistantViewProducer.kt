package dev.paulshields.assistantview

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import dev.paulshields.assistantview.factories.AssistantViewFactory
import dev.paulshields.assistantview.services.FileOpenerService
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.lok.logInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AssistantViewProducer : ToolWindowFactory, KoinComponent {
    private val assistantViewFactory: AssistantViewFactory by inject()
    private val assistantViewService: AssistantViewService by inject()
    private val fileOpenerService: FileOpenerService by inject()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        logInfo { "Creating Assistant View for project ${project.name}." }
        val assistantView = assistantViewFactory.createAssistantView(toolWindow, project)

        assistantViewService.registerAssistantViewForProject(assistantView, project)
        fileOpenerService.openCounterpartForTabInFocus(project)

        Disposer.register(project) {
            logInfo { "Project close detected. Closing Assistant View for ${project.name}." }
            assistantViewService.closeAssistantViewForProject(project)
        }
    }
}
