package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class FileManagerService {
    fun getFileFromProject(rawVirtualFile: VirtualFile, project: Project) =
        project.getService(PsiManager::class.java)
            .findFile(rawVirtualFile)
            ?.let { AssistantViewFile(it) }

    fun getFileFromProject(assistantViewClass: AssistantViewClass, project: Project): AssistantViewFile? {
        val virtualFileForClass = assistantViewClass.underlyingPsiClass.containingFile.virtualFile
        return getFileFromProject(virtualFileForClass, project)
    }
}
