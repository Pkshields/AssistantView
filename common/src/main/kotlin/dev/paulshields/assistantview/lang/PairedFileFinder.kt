package dev.paulshields.assistantview.lang

interface PairedFileFinder {
    fun findPairedFile(file: AssistantViewFile): AssistantViewFile?
}
