package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.Test

class FileAssistantServiceTest {
    private val fileManagerService = mock<FileManagerService>()

    private val project = mock<Project>()
    private val assistantViewClass = mock<AssistantViewClass>().apply {
        every { baseClass } returns null
        every { interfaces } returns emptyList()
    }
    private val assistantViewFile = mock<AssistantViewFile>().apply {
        every { mainClass } returns assistantViewClass
    }

    private val target = FileAssistantService(fileManagerService)

    @Test
    fun `test should return file containing counterpart base class`() {
        val fileContainingBaseClass = mock<AssistantViewFile>()
        val baseClass = mock<AssistantViewClass>()
        every { assistantViewClass.baseClass } returns baseClass
        every { fileManagerService.getFileFromProject(baseClass, project) } returns fileContainingBaseClass

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, equalTo(fileContainingBaseClass))
    }

    @Test
    fun `test should return file containing first interface if no base class`() {
        val fileContainingInterface = mock<AssistantViewFile>()
        val firstInterface = mock<AssistantViewClass>()
        val secondInterface = mock<AssistantViewClass>()
        every { assistantViewClass.interfaces } returns listOf(firstInterface, secondInterface)
        every { fileManagerService.getFileFromProject(firstInterface, project) } returns fileContainingInterface

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, equalTo(fileContainingInterface))
    }

    @Test
    fun `test should handle file with no classes in it`() {
        every { assistantViewFile.mainClass } returns null

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, absent())
    }

    @Test
    fun `test should handle class with no base class or interfaces`() {
        every { assistantViewFile.mainClass } returns assistantViewClass
        every { assistantViewClass.baseClass } returns null
        every { assistantViewClass.interfaces } returns emptyList()

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, absent())
    }
}
