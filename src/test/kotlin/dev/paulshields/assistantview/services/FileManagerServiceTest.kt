package dev.paulshields.assistantview.services

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import dev.paulshields.assistantview.services.intellij.IntellijFileSystemService
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.files.KotlinAssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.Test

class FileManagerServiceTest {
    private val virtualFile = mock<VirtualFile>()
    private val kotlinFile = mock<KtFile>()
    private val psiManager = mock<PsiManager>().apply {
        every { findFile(virtualFile) } returns kotlinFile
    }
    private val project = mock<Project>().apply {
        every { getService(PsiManager::class.java) } returns psiManager
    }

    private val fileName = "KotlinFile.kt"
    private val intellijFileSystemService = mock<IntellijFileSystemService>().apply {
        every { getAllFilenames(project) } returns listOf(fileName)
        every { findVirtualFileByFilename(fileName, project) } returns virtualFile
    }

    private val unsupportedFile = mock<PsiFile>()
    private val assistantViewClass = mock<AssistantViewClass>().apply {
        every { psiClass.containingFile.virtualFile } returns virtualFile
        every { project } returns this@FileManagerServiceTest.project
    }

    private val target = FileManagerService(intellijFileSystemService)

    @Test
    fun `should get assistant view file from kotlin virtualfile`() {
        val result = target.getFileFromVirtualFile(virtualFile, project)

        assertThat(result).isNotNull().isInstanceOf(KotlinAssistantViewFile::class)
        assertThat(result?.psiFile).isEqualTo(kotlinFile)
    }

    @Test
    fun `should return null if code in virtualfile is unsupported`() {
        every { psiManager.findFile(virtualFile) } returns unsupportedFile

        val result = target.getFileFromVirtualFile(virtualFile, project)

        assertThat(result).isNull()
    }

    @Test
    fun `should return null if there is no underlying code file in virtualfile`() {
        every { psiManager.findFile(virtualFile) } returns null

        val result = target.getFileFromVirtualFile(virtualFile, project)

        assertThat(result).isNull()
    }

    @Test
    fun `should get assistant view file from class`() {
        val result = target.getFileFromClass(assistantViewClass)

        assertThat(result).isNotNull().isInstanceOf(KotlinAssistantViewFile::class)
        assertThat(result?.psiFile).isEqualTo(kotlinFile)
    }

    @Test
    fun `should return null if the class is unsupported`() {
        every { psiManager.findFile(virtualFile) } returns unsupportedFile

        val result = target.getFileFromClass(assistantViewClass)

        assertThat(result).isNull()
    }

    @Test
    fun `should return null if the class has no file in current project`() {
        every { psiManager.findFile(virtualFile) } returns null

        val result = target.getFileFromClass(assistantViewClass)

        assertThat(result).isNull()
    }

    @Test
    fun `should get assistant view file whose filename matches regex`() {
        val result = target.findFilesMatchingRegex(fileName.toRegex(), project)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isInstanceOf(KotlinAssistantViewFile::class)
        assertThat(result[0].psiFile).isEqualTo(kotlinFile)
    }

    @Test
    fun `should return empty list if no filenames match regex`() {
        val result = target.findFilesMatchingRegex("InvalidFileName.java".toRegex(), project)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list if no virtualfile available for any files that match regex`() {
        every { intellijFileSystemService.findVirtualFileByFilename(fileName, project) } returns null

        val result = target.findFilesMatchingRegex(fileName.toRegex(), project)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list if code in virtualfiles that match regex is unsupported`() {
        every { psiManager.findFile(virtualFile) } returns unsupportedFile

        val result = target.findFilesMatchingRegex(fileName.toRegex(), project)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list if there is no underlying code file in files that match regex`() {
        every { psiManager.findFile(virtualFile) } returns null

        val result = target.findFilesMatchingRegex(fileName.toRegex(), project)

        assertThat(result).isEmpty()
    }
}
