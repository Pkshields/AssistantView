package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import dev.paulshields.assistantview.services.intellij.IntellijFileSystemService
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.sourcefiles.files.JavaAssistantViewFile
import dev.paulshields.assistantview.sourcefiles.files.KotlinAssistantViewFile
import dev.paulshields.lok.logWarn
import org.jetbrains.kotlin.psi.KtFile

class FileManagerService(private val intellijFileSystemService: IntellijFileSystemService) {
    fun getFileFromVirtualFile(virtualFile: VirtualFile, project: Project): AssistantViewFile? {
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)

        return when (psiFile) {
            is KtFile -> KotlinAssistantViewFile(psiFile, project)
            is PsiJavaFile -> JavaAssistantViewFile(psiFile, project)
            else -> {
                logWarn { "File type for file ${psiFile?.name} is not supported by Assistant View." }
                null
            }
        }
    }

    fun getFileFromClass(clazz: AssistantViewClass): AssistantViewFile? {
        val virtualFile = clazz.psiClass.containingFile.virtualFile
        return getFileFromVirtualFile(virtualFile, clazz.project)
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
