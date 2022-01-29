package dev.paulshields.assistantview.services.intellij

import com.intellij.openapi.project.Project
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

class IntellijFileSystemService {
    fun getAllFilenames(project: Project) = FilenameIndex.getAllFilenames(project).filter { !it.endsWith(".class") }

    fun findVirtualFileByFilename(name: String, project: Project) =
        FilenameIndex.getVirtualFilesByName(project, name, GlobalSearchScope.allScope(project)).firstOrNull()
}
