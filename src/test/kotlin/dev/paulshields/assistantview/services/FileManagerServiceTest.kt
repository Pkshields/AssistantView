package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.kotlin.psi.KtFile
import org.junit.Ignore
import org.junit.Test

class FileManagerServiceTest {

    private val virtualFile = mock<VirtualFile>()
    private val psiManager = mock<PsiManager>()

    private val project = mock<Project> {
        on { getService(PsiManager::class.java) } doReturn psiManager
    }

    private val target = FileManagerService()

    @Test
    @Ignore("To be fixed when AssistantViewFile makes liberal use of the lazy")
    fun `test should get file from project using virtual files`() {
        val psiFileBehindVirtualFile = mock<KtFile>()
        whenever(psiManager.findFile(virtualFile)).thenReturn(psiFileBehindVirtualFile)

        val result = target.getFileFromProject(virtualFile, project)

        assertThat(result?.underlyingPsiFile, equalTo(psiFileBehindVirtualFile as PsiFile))
    }

    @Test
    fun `test should handle virtual file which is not in project`() {
        whenever(psiManager.findFile(virtualFile)).thenReturn(null)

        val result = target.getFileFromProject(virtualFile, project)

        assertThat(result?.underlyingPsiFile, absent())
    }

    @Test
    @Ignore("To be implemented when we switch to MockK")
    fun `test should get file containing class from project`() { }

    @Test
    @Ignore("To be implemented when we switch to MockK")
    fun `test should handle class which is not in project`() { }
}
