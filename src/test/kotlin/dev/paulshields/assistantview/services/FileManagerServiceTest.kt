package dev.paulshields.assistantview.services

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import dev.paulshields.assistantview.sourcefiles.files.KotlinAssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.Test

class FileManagerServiceTest {
    private val psiManager = mock<PsiManager>()
    private val virtualFile = mock<VirtualFile>()
    private val project = mock<Project>().apply {
        every { getService(PsiManager::class.java) } returns psiManager
    }
    private val kotlinFile = mock<KtFile>().apply {
        every { name } returns "KotlinFile"
    }

    private val target = FileManagerService()

    @Test
    fun `should get assistant view file from kotlin virtualfile`() {
        every { psiManager.findFile(virtualFile) } returns kotlinFile

        val result = target.getFileFromVirtualFile(virtualFile, project)

        assertThat(result).isNotNull().isInstanceOf(KotlinAssistantViewFile::class)
        assertThat(result?.psiFile).isEqualTo(kotlinFile)
    }

    @Test
    fun `should return null if there is no underlying code file in virtualfile`() {
        val virtualFile = mock<VirtualFile>()
        every { psiManager.findFile(virtualFile) } returns null

        val result = target.getFileFromVirtualFile(virtualFile, project)

        assertThat(result).isNull()
    }
}
