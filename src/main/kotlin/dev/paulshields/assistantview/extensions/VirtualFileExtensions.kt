package dev.paulshields.assistantview.extensions

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

fun VirtualFile.toAssistantViewFile(project: Project) = AssistantViewFile.fromVirtualFile(this, project)
