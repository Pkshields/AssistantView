package dev.paulshields.assistantview.sourcefiles.files

import com.intellij.psi.PsiJavaFile
import dev.paulshields.assistantview.sourcefiles.AssistantViewFileTest
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every

class JavaAssistantViewFileTest : AssistantViewFileTest() {
    private val javaFile = mock<PsiJavaFile>().apply {
        every { name } returns fileName
        every { classes } returns arrayOf(mainClass, mainClass)
    }

    override val target = JavaAssistantViewFile(javaFile, project)
}
