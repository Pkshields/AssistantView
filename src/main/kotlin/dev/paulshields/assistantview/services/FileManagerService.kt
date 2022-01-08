package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.sourcefiles.files.KotlinAssistantViewFile
import dev.paulshields.lok.logWarn
import org.jetbrains.kotlin.psi.KtFile

class FileManagerService {
    fun getFileFromVirtualFile(virtualFile: VirtualFile, project: Project): AssistantViewFile? {
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)

        return when (psiFile) {
            is KtFile -> KotlinAssistantViewFile(psiFile)
            else -> {
                logWarn { "File type for file ${psiFile?.name} is not supported by Assistant View." }
                null
            }
        }
    }
}
