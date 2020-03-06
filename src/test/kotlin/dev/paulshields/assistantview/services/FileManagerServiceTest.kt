package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.relaxedMock
import io.mockk.every
import org.jetbrains.kotlin.psi.KtFile
import org.junit.Ignore
import org.junit.Test

class FileManagerServiceTest {

    private val virtualFile = mock<VirtualFile>()
    private val psiManager = mock<PsiManager>()

    private val project = mock<Project>().apply {
        every { getService(PsiManager::class.java) } returns psiManager
    }

    private val target = FileManagerService()

    @Test
    @Ignore("To be fixed when AssistantViewFile makes liberal use of the lazy")
    fun `test should get file from project using virtual files`() {
        val psiFileBehindVirtualFile = mock<KtFile>()
        every { psiManager.findFile(virtualFile) } returns psiFileBehindVirtualFile

        val result = target.getFileFromProject(virtualFile, project)

        assertThat(result?.underlyingPsiFile, equalTo(psiFileBehindVirtualFile as PsiFile))
    }

    @Test
    fun `test should handle virtual file which is not in project`() {
        every { psiManager.findFile(virtualFile) } returns null

        val result = target.getFileFromProject(virtualFile, project)

        assertThat(result?.underlyingPsiFile, absent())
    }

    @Test
    fun `test should get file containing class from project`() {
        val assistantViewClass = mock<AssistantViewClass>().apply {
            every { underlyingPsiClass.containingFile.virtualFile } returns virtualFile
        }
        val psiFileBehindVirtualFile = relaxedMock<KtFile>()
        every { psiManager.findFile(virtualFile) } returns psiFileBehindVirtualFile

        val result = target.getFileFromProject(assistantViewClass, project)

        assertThat(result?.underlyingPsiFile, equalTo(psiFileBehindVirtualFile as PsiFile))
    }

    @Test
    fun `test should handle class which is not in project`() {
        val assistantViewClass = mock<AssistantViewClass>().apply {
            every { underlyingPsiClass.containingFile.virtualFile } returns virtualFile
        }
        every { psiManager.findFile(virtualFile) } returns null

        val result = target.getFileFromProject(assistantViewClass, project)

        assertThat(result?.underlyingPsiFile, absent())
    }
}
