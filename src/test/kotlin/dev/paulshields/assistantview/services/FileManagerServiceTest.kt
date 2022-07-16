package dev.paulshields.assistantview.services

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import dev.paulshields.assistantview.lang.AssistantViewClass
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.assistantview.lang.SourceFileInterpreter
import dev.paulshields.assistantview.services.intellij.IntellijExtensionPoints
import dev.paulshields.assistantview.services.intellij.IntellijFileSystemService
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class FileManagerServiceTest {
    private val virtualFile = mock<VirtualFile>()
    private val assistantViewFile = mock<AssistantViewFile>()
    private val supportedFile = mock<PsiFile>()
    private val unsupportedFile = mock<PsiFile>()
    private val name = "KotlinFile"
    private val fileName = "$name.kt"

    private val psiManager = mock<PsiManager>().apply {
        every { findFile(virtualFile) } returns supportedFile
    }
    private val project = mock<Project>().apply {
        every { getService(PsiManager::class.java) } returns psiManager
    }
    private val supportedFileInterpreter = mock<SourceFileInterpreter>().apply {
        every { parseFile(supportedFile, project) } returns assistantViewFile
        every { parseFile(unsupportedFile, any()) } returns null
    }
    private val interpreters = mock<ExtensionPointName<SourceFileInterpreter>>().apply {
        every { extensionList } returns listOf(supportedFileInterpreter)
    }
    private val assistantViewClass = mock<AssistantViewClass>().apply {
        every { containingFile } returns virtualFile
        every { project } returns this@FileManagerServiceTest.project
    }

    private val intellijFileSystemService = mock<IntellijFileSystemService>().apply {
        every { getAllFilenames(project) } returns listOf(fileName)
        every { findVirtualFileByFilename(fileName, project) } returns virtualFile
    }
    private val intellijExtensionPoints = mock<IntellijExtensionPoints>().apply {
        every { sourceFileInterpreters } returns interpreters
    }

    private val target = FileManagerService(intellijFileSystemService, intellijExtensionPoints)

    @Test
    fun `should get assistant view file from virtualfile supported by injected parsers`() {
        val result = target.getFileFromVirtualFile(virtualFile, project)

        assertThat(result).isEqualTo(assistantViewFile)
    }

    @Test
    fun `should return null if code in virtualfile is unsupported by injected parsers`() {
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
    fun `should get assistant view file from class type that is supported by the injected parsers`() {
        val result = target.getFileFromClass(assistantViewClass)

        assertThat(result).isEqualTo(assistantViewFile)
    }

    @Test
    fun `should return null if the class is unsupported`() {
        every { psiManager.findFile(virtualFile) } returns unsupportedFile

        val result = target.getFileFromClass(assistantViewClass)

        assertThat(result).isNull()
    }

    @Test
    fun `should return null if the class has no file in current project`() {
        every { assistantViewClass.containingFile } returns null

        val result = target.getFileFromClass(assistantViewClass)

        assertThat(result).isNull()
    }

    @Test
    fun `should return null if the class has no file containing code in current project`() {
        every { psiManager.findFile(virtualFile) } returns null

        val result = target.getFileFromClass(assistantViewClass)

        assertThat(result).isNull()
    }

    @Test
    fun `should get assistant view file for name of file`() {
        val result = target.findFileWithName(name, project)

        assertThat(result).isEqualTo(assistantViewFile)
    }

    @Test
    fun `should return empty list if no file with name exist`() {
        val result = target.findFileWithName("InvalidFileName", project)

        assertThat(result).isNull()
    }

    @Test
    fun `should return null if no virtualfile available for file with name`() {
        every { intellijFileSystemService.findVirtualFileByFilename(fileName, project) } returns null

        val result = target.findFileWithName(name, project)

        assertThat(result).isNull()
    }

    @Test
    fun `should return null if code in virtualfiles for file with name is unsupported by the injected parsers`() {
        every { psiManager.findFile(virtualFile) } returns unsupportedFile

        val result = target.findFileWithName(name, project)

        assertThat(result).isNull()
    }

    @Test
    fun `should return null if there is no underlying code file for file with name`() {
        every { psiManager.findFile(virtualFile) } returns null

        val result = target.findFileWithName(name, project)

        assertThat(result).isNull()
    }

    @Test
    fun `should get assistant view file whose filename matches regex`() {
        val result = target.findFilesMatchingRegex(fileName.toRegex(), project)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(assistantViewFile)
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
    fun `should return empty list if code in virtualfiles that match regex is unsupported by the injected parsers`() {
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
