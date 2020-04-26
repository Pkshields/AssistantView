package dev.paulshields.assistantview.services

import dev.paulshields.assistantview.AssistantView
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile

class AssistantViewService {
    private var assistantView: AssistantView? = null

    fun registerAssistantView(assistantView: AssistantView) {
        this.assistantView = assistantView
    }

    fun openFile(file: AssistantViewFile) = assistantView?.openFile(file)
}
