package dev.paulshields.assistantview.services

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.lang.AssistantViewClass
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.assistantview.lang.PairedFileFinder
import dev.paulshields.assistantview.services.intellij.IntellijExtensionPoints
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

class FileAssistantServiceTest {
    private val project = mock<Project>()
    private val file = mock<AssistantViewFile>().apply {
        every { name } returns "LuigiUnitTest"
        every { mainClass?.superClasses } returns emptyList()
        every { mainClass?.interfaces } returns emptyList()
        every { project } returns this@FileAssistantServiceTest.project
    }
    private val counterpartFile = mock<AssistantViewFile>()
    private val pairedFileFinder = mock<PairedFileFinder>().apply {
        every { findPairedFile(file) } returns null
    }
    private val fileManagerService = mock<FileManagerService>().apply {
        every { findFileWithName(any(), project) } returns null
    }
    private val intellijExtensionPoints = mock<IntellijExtensionPoints>().apply {
        every { pairedFileFinders.extensionList } returns listOf(pairedFileFinder)
    }

    private val target = FileAssistantService(fileManagerService, intellijExtensionPoints)

    @Test
    fun `should return super class as counterpart class`() {
        val superClass = mock<AssistantViewClass>()
        every { file.mainClass?.superClasses } returns listOf(superClass)
        every { fileManagerService.getFileFromClass(superClass) } returns counterpartFile

        val result = target.getCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should return interface as counterpart class if no base class available`() {
        val firstInterface = mock<AssistantViewClass>()
        every { file.mainClass?.interfaces } returns listOf(firstInterface)
        every { fileManagerService.getFileFromClass(firstInterface) } returns counterpartFile

        val result = target.getCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should return test suite as counterpart class if no base class or interface available`() {
        every { fileManagerService.findFilesMatchingRegex(match { it.pattern.endsWith("Test") }, project) } returns listOf(counterpartFile)

        val result = target.getCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should return target class as counterpart class if a test suite has been opened`() {
        every { file.name } returns "MarioTest"
        every { fileManagerService.findFileWithName(eq("Mario"), project) } returns counterpartFile

        val result = target.getCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should return target class as counterpart class if a unit test suite has been opened`() {
        every { file.name } returns "MarioUnitTest"
        every { fileManagerService.findFileWithName(eq("Mario"), project) } returns counterpartFile

        val result = target.getCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should fall back to standard counterpart class logic if can not find target class for test suite`() {
        every { file.name } returns "MarioUnitTest"
        every { fileManagerService.findFileWithName(any(), project) } returns null

        target.getCounterpartFile(file)

        verify {
            pairedFileFinder.findPairedFile(file)
            fileManagerService.findFileWithName(any(), project)
            file.mainClass?.superClasses
            file.mainClass?.interfaces
        }
    }

    @Test
    fun `should find and return language specific paired file`() {
        every { pairedFileFinder.findPairedFile(file) } returns counterpartFile

        val result = target.getCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should fall back to standard counterpart class logic if no paired file is found`() {
        every { pairedFileFinder.findPairedFile(file) } returns null

        target.getCounterpartFile(file)

        verify {
            pairedFileFinder.findPairedFile(file)
            fileManagerService.findFileWithName(any(), project)
            file.mainClass?.superClasses
            file.mainClass?.interfaces
        }
    }

    @Test
    fun `should fall back to standard counterpart class logic if no paired file finders are available`() {
        every { intellijExtensionPoints.pairedFileFinders.extensionList } returns emptyList()

        target.getCounterpartFile(file)

        verify {
            pairedFileFinder.findPairedFile(file)
            fileManagerService.findFileWithName(any(), project)
            file.mainClass?.superClasses
            file.mainClass?.interfaces
        }
    }

    @Test
    fun `should return null if no counterpart class available`() {
        val result = target.getCounterpartFile(file)

        assertThat(result).isNull()
    }
}
