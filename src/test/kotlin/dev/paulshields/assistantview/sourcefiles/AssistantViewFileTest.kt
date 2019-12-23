package dev.paulshields.assistantview.sourcefiles

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.apache.commons.lang.NotImplementedException
import org.jetbrains.kotlin.psi.KtFile
import org.junit.Test

class AssistantViewFileTest {

    private val virtualFile = mock<VirtualFile>()
    private val ktFile = mock<KtFile> {
        on { classes } doReturn emptyArray()
    }
    private val psiManager = mock<PsiManager> {
        on { findFile(virtualFile) } doReturn ktFile
    }
    private val project = mock<Project> {
        on { getService(any<Class<PsiManager>>()) } doReturn psiManager
    }

    @Test
    fun `test should create from virtual file`() {
        val result = AssistantViewFile.fromVirtualFile(virtualFile, project)

        assertThat(result?.psiFile, equalTo(ktFile as PsiFile))
    }

    @Test
    fun `test should return null if virtual file is not in project`() {
        whenever(psiManager.findFile(virtualFile)).thenReturn(null)

        val result = AssistantViewFile.fromVirtualFile(virtualFile, project)

        assertThat(result, absent())
    }

    @Test
    fun `test should return class list from kotlin file`() {
        val psiClass = mock<PsiClass>()
        whenever(ktFile.classes).thenReturn(arrayOf(psiClass))

        val target = AssistantViewFile.fromVirtualFile(virtualFile, project)!!
        val result = target.classes

        assertThat(result.size, equalTo(1))
        assertThat(result[0].psiClass, equalTo(psiClass))
    }

    @Test
    fun `test should handle empty class list on kotlin file`() {
        val target = AssistantViewFile.fromVirtualFile(virtualFile, project)!!
        val result = target.classes

        assertThat(result.size, equalTo(0))
    }

    @Test(expected = NotImplementedException::class)
    fun `test should throw exception when getting class list if psifile is not supported`() {
        val unsupportedPsiFile = mock<PsiFile>()
        whenever(psiManager.findFile(virtualFile)).thenReturn(unsupportedPsiFile)

        val target = AssistantViewFile.fromVirtualFile(virtualFile, project)!!
        val result = target.classes
    }

    @Test
    fun `test should define first class in class list as main class`() {
        val psiClass = mock<PsiClass>()
        whenever(ktFile.classes).thenReturn(arrayOf(psiClass))

        val target = AssistantViewFile.fromVirtualFile(virtualFile, project)!!
        val result = target.mainClass

        assertThat(result?.psiClass, equalTo(psiClass))
    }

    @Test
    fun `test should handle empty class list when defining main class`() {
        val target = AssistantViewFile.fromVirtualFile(virtualFile, project)!!
        val result = target.mainClass

        assertThat(result, absent())
    }
}
