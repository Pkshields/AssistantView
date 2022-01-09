package dev.paulshields.assistantview.services

import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class FileAssistantService(private val fileManagerService: FileManagerService) {
    fun getCounterpartFile(file: AssistantViewFile): AssistantViewFile? {
        val counterpartClass = file.mainClass?.let {
            it.superClass ?: it.interfaces.firstOrNull()
        }

        return counterpartClass?.let { fileManagerService.getFileFromClass(it) }
    }
}
