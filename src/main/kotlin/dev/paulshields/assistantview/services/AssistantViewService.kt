package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.AssistantView
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.lok.logError
import dev.paulshields.lok.logWarn

class AssistantViewService {
    private val assistantViews = mutableMapOf<Project, AssistantView>()

    fun registerAssistantViewForProject(assistantView: AssistantView, project: Project) {
        if (assistantViews.containsKey(project)) {
            logWarn { "Warning: Assistant view already exists for project ${project.name}. Overwriting..." }
        }

        assistantViews[project] = assistantView
    }

    fun closeAssistantViewForProject(project: Project) {
        assistantViews.remove(project)
    }

    fun openFile(assistantViewFile: AssistantViewFile) =
        assistantViews[assistantViewFile.project]?.openFile(assistantViewFile)
            ?: run { logError { "No assistant view available for project ${assistantViewFile.project.name}. Ignoring request to open file." } }
}
