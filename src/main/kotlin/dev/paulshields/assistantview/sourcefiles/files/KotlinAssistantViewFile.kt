package dev.paulshields.assistantview.sourcefiles.files

import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import org.jetbrains.kotlin.psi.KtFile

class KotlinAssistantViewFile(private val ktFile: KtFile) : AssistantViewFile(ktFile) {
    override fun getClassesFromFile() = ktFile.classes.map(::AssistantViewClass).toList()
}
