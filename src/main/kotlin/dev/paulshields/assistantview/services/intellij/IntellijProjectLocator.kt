package dev.paulshields.assistantview.services.intellij

import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VirtualFile

class IntellijProjectLocator {
    fun getProjectForVirtualFile(file: VirtualFile) = ProjectLocator.getInstance().guessProjectForFile(file)
}
