package dev.paulshields.assistantview.services

import dev.paulshields.assistantview.extensions.extractGroups
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.assistantview.services.intellij.IntellijExtensionPoints

class CounterpartFileService(
    private val fileManagerService: FileManagerService,
    intellijExtensionPoints: IntellijExtensionPoints) {

    private val pairedFileFinders = intellijExtensionPoints.pairedFileFinders.extensionList
    private val fileNameTestSuiteRegex = "(\\w+?)(Unit)?Test".toRegex()

    fun findCounterpartFile(file: AssistantViewFile) =
        findLanguageSpecificPairedFile(file)
            ?: findPairedFileByFileName(file)
            ?: findTargetFileIfTestSuite(file)
            ?: findFileFromExtendsClasses(file)
            ?: findUnitTestForFile(file)

    private fun findLanguageSpecificPairedFile(file: AssistantViewFile) =
        pairedFileFinders.firstNotNullOfOrNull { it.findPairedFile(file) }

    private fun findPairedFileByFileName(file: AssistantViewFile) =
        fileManagerService
            .findFilesMatchingRegex("^${file.name}\\.(?!${file.extension})".toRegex(), file.project)
            .firstOrNull()

    private fun findTargetFileIfTestSuite(file: AssistantViewFile): AssistantViewFile? {
        val name = fileNameTestSuiteRegex
            .extractGroups(file.name)
            .getOrNull(0)
        return name?.let { fileManagerService.findFileWithName(it, file.project) }
    }

    private fun findFileFromExtendsClasses(file: AssistantViewFile): AssistantViewFile? {
        val extendsClass = file.mainClass?.let {
            it.superClasses.firstOrNull() ?: it.interfaces.firstOrNull()
        }

        return extendsClass?.let { fileManagerService.getFileFromClass(it) }
    }

    private fun findUnitTestForFile(file: AssistantViewFile) =
        fileManagerService
            .findFilesMatchingRegex("${file.name}(Unit)?Test".toRegex(), file.project)
            .firstOrNull()
}
