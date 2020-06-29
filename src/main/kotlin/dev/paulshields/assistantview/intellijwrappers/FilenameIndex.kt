package dev.paulshields.assistantview.intellijwrappers

import com.intellij.openapi.project.Project
import com.intellij.psi.search.FilenameIndex as IntellijFilenameIndex
import com.intellij.psi.search.GlobalSearchScope

class FilenameIndex {
    fun getAllFilenames(project: Project) = IntellijFilenameIndex.getAllFilenames(project)

    fun getFilesByName(project: Project, name: String, scope: GlobalSearchScope) =
        IntellijFilenameIndex.getFilesByName(project, name, scope)
}
