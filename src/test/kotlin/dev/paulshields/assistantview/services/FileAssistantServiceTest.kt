package dev.paulshields.assistantview.services

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.lang.AssistantViewClass
import dev.paulshields.assistantview.lang.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test

class FileAssistantServiceTest {
    private val project = mock<Project>()
    private val fileManagerService = mock<FileManagerService>()
    private val file = mock<AssistantViewFile>().apply {
        every { mainClass?.superClass } returns null
        every { mainClass?.interfaces } returns emptyList()
        every { project } returns this@FileAssistantServiceTest.project
    }
    private val counterpartFile = mock<AssistantViewFile>()

    private val target = FileAssistantService(fileManagerService)

    @Test
    fun `should return super class as counterpart class`() {
        val superClass = mock<AssistantViewClass>()
        every { file.mainClass?.superClass } returns superClass
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
    fun `should fall back to standard counterpart class logic if can not find target class for test suite `() {
        every { file.name } returns "MarioUnitTest"
        every { fileManagerService.findFileWithName(any(), project) } returns null

        target.getCounterpartFile(file)

        verify {
            fileManagerService.findFileWithName(any(), project)
            file.mainClass?.superClass
            file.mainClass?.interfaces
        }
    }

    @Test
    fun `should return null if no counterpart class available`() {
        val result = target.getCounterpartFile(file)

        assertThat(result).isNull()
    }
}
