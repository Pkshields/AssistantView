package dev.paulshields.assistantview.services

import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class FileAssistantService(private val fileManagerService: FileManagerService) {
    fun getCounterpartFile(file: AssistantViewFile): AssistantViewFile? {
        val extendsClass = file.mainClass?.let {
            it.superClass ?: it.interfaces.firstOrNull()
        }

        return extendsClass?.let { fileManagerService.getFileFromClass(it) }
            ?: findUnitTestForFile(file)
    }

    private fun findUnitTestForFile(file: AssistantViewFile) =
        fileManagerService
            .findFilesMatchingRegex("${file.name}(Unit)?Test".toRegex(), file.project)
            .firstOrNull()
}
