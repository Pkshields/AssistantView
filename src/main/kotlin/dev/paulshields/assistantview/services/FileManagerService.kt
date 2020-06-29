package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import dev.paulshields.assistantview.intellijwrappers.FilenameIndex
import dev.paulshields.assistantview.intellijwrappers.PsiTypesUtil
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.sourcefiles.AssistantViewType

class FileManagerService(
    private val filenameIndex: FilenameIndex,
    private val psiTypesUtil: PsiTypesUtil
) {
    fun getFileFromProject(rawVirtualFile: VirtualFile, project: Project) =
        project.getService(PsiManager::class.java)
            .findFile(rawVirtualFile)
            ?.let { AssistantViewFile(it) }

    fun getFileFromProject(assistantViewClass: AssistantViewClass, project: Project): AssistantViewFile? {
        val virtualFileForClass = assistantViewClass.underlyingPsiClass.containingFile.virtualFile
        return getFileFromProject(virtualFileForClass, project)
    }

    fun getFileFromProject(assistantViewType: AssistantViewType, project: Project) =
        psiTypesUtil.getPsiClass(assistantViewType.underlyingPsiType)
            ?.let { getFileFromProject(AssistantViewClass(it), project) }

    fun findFileByName(name: String, project: Project): AssistantViewFile? {
        val allFilesNames = filenameIndex.getAllFilenames(project)

        val foundFile = allFilesNames.firstOrNull { it.startsWith("$name.") }
            ?: allFilesNames.firstOrNull { it.contains(name) }

        return foundFile?.let {
            filenameIndex.getFilesByName(project, it, GlobalSearchScope.allScope(project))
                .firstOrNull()
                ?.let { AssistantViewFile(it) }
        }
    }
}
