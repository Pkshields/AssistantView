package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import dev.paulshields.assistantview.lang.AssistantViewClass
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.assistantview.services.intellij.IntellijExtensionPoints
import dev.paulshields.assistantview.services.intellij.IntellijFileSystemService
import dev.paulshields.lok.logWarn

class FileManagerService(
    private val intellijFileSystemService: IntellijFileSystemService,
    intellijExtensionPoints: IntellijExtensionPoints) {

    private val sourceFileInterpreters = intellijExtensionPoints.sourceFileInterpreters.extensionList

    fun getFileFromVirtualFile(virtualFile: VirtualFile, project: Project): AssistantViewFile? {
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)

        return psiFile?.let {
            sourceFileInterpreters.firstNotNullOfOrNull { it.parseFile(psiFile, project) }
        } ?: run {
            logWarn { "File type for file ${psiFile?.name} is not supported by Assistant View." }
            null
        }
    }

    fun getFileFromClass(clazz: AssistantViewClass) =
        clazz.containingFile?.let {
            getFileFromVirtualFile(it, clazz.project)
        } ?: run {
            logWarn { "Could not find the file containing the class ${clazz.name} in the project." }
            null
        }

    fun findFileWithName(name: String, project: Project): AssistantViewFile? {
        val stringToMatch = "$name."

        return intellijFileSystemService
            .getAllFilenames(project)
            .firstOrNull { it.startsWith(stringToMatch) }
            ?.let {
                intellijFileSystemService.findVirtualFileByFilename(it, project)
                    ?.let { getFileFromVirtualFile(it, project) }
            }
    }

    fun findFilesMatchingRegex(regex: Regex, project: Project) =
        intellijFileSystemService
            .getAllFilenames(project)
            .filter { regex.containsMatchIn(it) }
            .mapNotNull {
                intellijFileSystemService.findVirtualFileByFilename(it, project)
                    ?.let { getFileFromVirtualFile(it, project) }
            }
}
