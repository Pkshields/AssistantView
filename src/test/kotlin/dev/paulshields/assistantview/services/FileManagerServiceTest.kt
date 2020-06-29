package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.paulshields.assistantview.intellijwrappers.FilenameIndex
import dev.paulshields.assistantview.intellijwrappers.PsiTypesUtil
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewType
import dev.paulshields.assistantview.testcommon.mock
import dev.paulshields.assistantview.testcommon.relaxedMock
import io.mockk.every
import org.jetbrains.kotlin.psi.KtFile
import org.junit.Test

class FileManagerServiceTest {
    private val virtualFile = mock<VirtualFile>()
    private val psiManager = mock<PsiManager>()

    private val project = mock<Project>().apply {
        every { getService(PsiManager::class.java) } returns psiManager
    }

    private val target = FileManagerService(FilenameIndex(), PsiTypesUtil())

    @Test
    fun `test should get file from project using virtual files`() {
        val psiFileBehindVirtualFile = relaxedMock<KtFile>()
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

    @Test
    fun `test should get file from type`() {
        val psiClass = relaxedMock<PsiClass>().apply {
            every { containingFile.virtualFile } returns virtualFile
        }
        val psiClassType = mock<PsiClassType>().apply {
            every { resolve() } returns psiClass
        }
        val assistantViewType = mock<AssistantViewType>().apply {
            every { underlyingPsiType } returns psiClassType
        }
        val psiFileBehindVirtualFile = relaxedMock<KtFile>()
        every { psiManager.findFile(virtualFile) } returns psiFileBehindVirtualFile

        val result = target.getFileFromProject(assistantViewType, project)

        assertThat(result?.underlyingPsiFile, equalTo(psiFileBehindVirtualFile as PsiFile))
    }

    @Test
    fun `test should handle when file for type is not in project`() {
        val psiClass = relaxedMock<PsiClass>().apply {
            every { containingFile.virtualFile } returns virtualFile
        }
        val psiClassType = mock<PsiClassType>().apply {
            every { resolve() } returns psiClass
        }
        val assistantViewType = mock<AssistantViewType>().apply {
            every { underlyingPsiType } returns psiClassType
        }
        every { psiManager.findFile(virtualFile) } returns null

        val result = target.getFileFromProject(assistantViewType, project)

        assertThat(result?.underlyingPsiFile, absent())
    }
}
