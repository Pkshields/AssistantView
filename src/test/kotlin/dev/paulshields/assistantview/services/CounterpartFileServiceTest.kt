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
import io.mockk.Ordering
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

class CounterpartFileServiceTest {
    private val alternateExtension = "cpp"
    private val project = mock<Project>()
    private val file = mock<AssistantViewFile>().apply {
        every { name } returns "LuigiUnitTest"
        every { extension } returns "kt"
        every { mainClass?.superClasses } returns emptyList()
        every { mainClass?.interfaces } returns emptyList()
        every { project } returns this@CounterpartFileServiceTest.project
    }
    private val counterpartFile = mock<AssistantViewFile>()
    private val pairedFileFinder = mock<PairedFileFinder>().apply {
        every { findPairedFile(file) } returns null
    }
    private val fileManagerService = mock<FileManagerService>().apply {
        every { findFileWithName(any(), project) } returns null
        every { findFilesMatchingRegex(any(), project) } returns emptyList()
    }
    private val intellijExtensionPoints = mock<IntellijExtensionPoints>().apply {
        every { pairedFileFinders.extensionList } returns listOf(pairedFileFinder)
    }

    private val target = CounterpartFileService(fileManagerService, intellijExtensionPoints)

    @Test
    fun `should find and return language specific paired file`() {
        every { pairedFileFinder.findPairedFile(file) } returns counterpartFile

        val result = target.findCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should fall back to standard counterpart class logic if no paired file is found`() {
        every { pairedFileFinder.findPairedFile(file) } returns null

        val result = target.findCounterpartFile(file)

        verifyResultIsNullAndCounterpartFileAlgorithmWasFollowedInOrder(result)
    }

    @Test
    fun `should fall back to standard counterpart class logic if no paired file finders are available`() {
        every { intellijExtensionPoints.pairedFileFinders.extensionList } returns emptyList()

        val result = target.findCounterpartFile(file)

        verifyResultIsNullAndCounterpartFileAlgorithmWasFollowedInOrder(result)
    }

    @Test
    fun `should find and return paired file by file name`() {
        val alternateFileName = "${file.name}.$alternateExtension"
        every { fileManagerService.findFilesMatchingRegex(match { it.containsMatchIn(alternateFileName) }, project) } returns listOf(counterpartFile)

        val result = target.findCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should not return currently opened file as counterpart file as paired file by file name`() {
        val fileName = "${file.name}.${file.extension}"
        every { fileManagerService.findFilesMatchingRegex(match { it.containsMatchIn(fileName) }, project) } returns listOf(file)

        val result = target.findCounterpartFile(file)

        verifyResultIsNullAndCounterpartFileAlgorithmWasFollowedInOrder(result)
    }

    @Test
    fun `should not return a file that has a similar file name but with a prefix`() {
        val alternateFileName = "Similar${file.name}.$alternateExtension"
        every { fileManagerService.findFilesMatchingRegex(match { it.containsMatchIn(alternateFileName) }, project) } returns listOf(counterpartFile)

        val result = target.findCounterpartFile(file)

        verifyResultIsNullAndCounterpartFileAlgorithmWasFollowedInOrder(result)
    }

    @Test
    fun `should not return a file that has a similar file name but with a suffix`() {
        val alternateFileName = "${file.name}Similar.$alternateExtension"
        every { fileManagerService.findFilesMatchingRegex(match { it.containsMatchIn(alternateFileName) }, project) } returns listOf(counterpartFile)

        val result = target.findCounterpartFile(file)

        verifyResultIsNullAndCounterpartFileAlgorithmWasFollowedInOrder(result)
    }

    @Test
    fun `should fall back to standard counterpart class logic if can not find any paired file by file name`() {
        every { fileManagerService.findFilesMatchingRegex(any(), project) } returns emptyList()

        val result = target.findCounterpartFile(file)

        verifyResultIsNullAndCounterpartFileAlgorithmWasFollowedInOrder(result)
    }

    @Test
    fun `should return super class as counterpart class`() {
        val superClass = mock<AssistantViewClass>()
        every { file.mainClass?.superClasses } returns listOf(superClass)
        every { fileManagerService.getFileFromClass(superClass) } returns counterpartFile

        val result = target.findCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should return interface as counterpart class if no base class available`() {
        val firstInterface = mock<AssistantViewClass>()
        every { file.mainClass?.interfaces } returns listOf(firstInterface)
        every { fileManagerService.getFileFromClass(firstInterface) } returns counterpartFile

        val result = target.findCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should return interface as counterpart class if super class file can not be resolved`() {
        val superClass = mock<AssistantViewClass>()
        val firstInterface = mock<AssistantViewClass>()
        every { fileManagerService.getFileFromClass(superClass) } returns null
        every { fileManagerService.getFileFromClass(firstInterface) } returns counterpartFile
        every { file.mainClass?.superClasses } returns listOf(superClass)
        every { file.mainClass?.interfaces } returns listOf(firstInterface)

        val result = target.findCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should return test suite as counterpart class if no base class or interface available`() {
        every { fileManagerService.findFilesMatchingRegex(match { it.pattern.endsWith("Test") }, project) } returns listOf(counterpartFile)

        val result = target.findCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should return target class as counterpart class if a test suite has been opened`() {
        every { file.name } returns "MarioTest"
        every { fileManagerService.findFileWithName(eq("Mario"), project) } returns counterpartFile

        val result = target.findCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should return target class as counterpart class if a unit test suite has been opened`() {
        every { file.name } returns "MarioUnitTest"
        every { fileManagerService.findFileWithName(eq("Mario"), project) } returns counterpartFile

        val result = target.findCounterpartFile(file)

        assertThat(result).isEqualTo(counterpartFile)
    }

    @Test
    fun `should fall back to standard counterpart class logic if can not find target class for test suite`() {
        every { file.name } returns "MarioUnitTest"
        every { fileManagerService.findFileWithName(any(), project) } returns null

        val result = target.findCounterpartFile(file)

        verifyResultIsNullAndCounterpartFileAlgorithmWasFollowedInOrder(result)
    }

    @Test
    fun `should return null if no counterpart class available`() {
        val result = target.findCounterpartFile(file)

        assertThat(result).isNull()
    }

    private fun verifyResultIsNullAndCounterpartFileAlgorithmWasFollowedInOrder(result: AssistantViewFile?) {
        assertThat(result).isNull()

        verify(ordering = Ordering.ORDERED) {
            pairedFileFinder.findPairedFile(file)
            fileManagerService.findFilesMatchingRegex(any(), project)
            fileManagerService.findFileWithName(any(), project)
            file.mainClass?.apply {
                superClasses
                interfaces
            }
            fileManagerService.findFilesMatchingRegex(any(), project)
        }
    }
}
