package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class FileAssistantService(private val fileManagerService: FileManagerService) {

    fun getCounterpartFile(openedFile: AssistantViewFile, project: Project) =
        openedFile.mainClass?.let {
            val counterpartClass = it.baseClass ?: it.interfaces.firstOrNull()
            counterpartClass?.let { fileManagerService.getFileFromProject(counterpartClass, project) }
        }
}
