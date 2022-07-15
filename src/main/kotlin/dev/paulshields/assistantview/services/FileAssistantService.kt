package dev.paulshields.assistantview.services

import dev.paulshields.assistantview.extensions.extractGroups
import dev.paulshields.assistantview.lang.AssistantViewFile

class FileAssistantService(private val fileManagerService: FileManagerService) {
    private val fileNameTestSuiteRegex = "(\\w+?)(Unit)?Test".toRegex()

    fun getCounterpartFile(file: AssistantViewFile) =
        findTargetFileIfTestSuite(file)
            ?: findFileFromExtendsClasses(file)
            ?: findUnitTestForFile(file)

    private fun findTargetFileIfTestSuite(file: AssistantViewFile): AssistantViewFile? {
        val name = fileNameTestSuiteRegex
            .extractGroups(file.name)
            .getOrNull(0)
        return name?.let { fileManagerService.findFileWithName(it, file.project) }
    }

    private fun findFileFromExtendsClasses(file: AssistantViewFile): AssistantViewFile? {
        val extendsClass = file.mainClass?.let {
            it.superClass ?: it.interfaces.firstOrNull()
        }

        return extendsClass?.let { fileManagerService.getFileFromClass(it) }
    }

    private fun findUnitTestForFile(file: AssistantViewFile) =
        fileManagerService
            .findFilesMatchingRegex("${file.name}(Unit)?Test".toRegex(), file.project)
            .firstOrNull()
}
