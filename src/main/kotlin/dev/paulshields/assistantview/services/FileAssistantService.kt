package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class FileAssistantService(private val fileManagerService: FileManagerService) {
    private val testSuiteClassNameRegex = Regex("(.+?)(?:[Uu]nit)?[Tt]est\$")

    fun getCounterpartFile(openedFile: AssistantViewFile, project: Project): AssistantViewFile? =
        openedFile.mainClass?.let {
            if (fileIsTestSuite(it)) {
                return getCounterpartFileForTestSuite(it, project)
            }

            return getCounterpartFileForGenericClass(it, project)
        }

    private fun fileIsTestSuite(openedClass: AssistantViewClass) = testSuiteClassNameRegex.matches(openedClass.name)

    private fun getCounterpartFileForTestSuite(openedClass: AssistantViewClass, project: Project) =
        getTargetFileForTestSuiteFromClassProperties(openedClass, project)
            ?: getTargetFileForTestSuiteFromClassName(openedClass, project)

    private fun getTargetFileForTestSuiteFromClassProperties(openedClass: AssistantViewClass, project: Project) =
        openedClass.properties
            .find { it.name.toLowerCase() == "target" }
            ?.let { fileManagerService.getFileFromProject(it.typeClass, project) }

    private fun getTargetFileForTestSuiteFromClassName(openedClass: AssistantViewClass, project: Project) =
        testSuiteClassNameRegex.find(openedClass.name)
            ?.takeIf { it.groups.count() > 1 }
            ?.let { it.groups[1]?.value }
            ?.let { fileManagerService.findFileByName(it, project) }

    private fun getCounterpartFileForGenericClass(openedClass: AssistantViewClass, project: Project): AssistantViewFile? {
        val counterpartClass = openedClass.baseClass ?: openedClass.interfaces.firstOrNull()
        return counterpartClass?.let { fileManagerService.getFileFromProject(counterpartClass, project) }
    }
}
