package dev.paulshields.assistantview.sourcefiles.files

import dev.paulshields.assistantview.sourcefiles.AssistantViewFileTest
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.jetbrains.kotlin.psi.KtFile

class KotlinAssistantViewFileTest : AssistantViewFileTest() {
    private val ktFile = mock<KtFile>().apply {
        every { name } returns fileName
        every { classes } returns arrayOf(mainClass, mainClass)
    }

    override val target = KotlinAssistantViewFile(ktFile, project)
}
