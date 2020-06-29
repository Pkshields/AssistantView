package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.sourcefiles.AssistantViewProperty
import dev.paulshields.assistantview.sourcefiles.AssistantViewType
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.Test

class FileAssistantServiceTest {
    private val fileManagerService = mock<FileManagerService>()

    private val project = mock<Project>()
    private val assistantViewClass = mock<AssistantViewClass>().apply {
        every { baseClass } returns null
        every { interfaces } returns emptyList()
        every { name } returns ""
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

    @Test
    fun `test should return file under test from test properties when opening test suite`() {
        val fileContainingClassUnderTest = mock<AssistantViewFile>()
        val typeOfClassUnderTest = mock<AssistantViewType>()
        val targetProperty = mock<AssistantViewProperty>().apply {
            every { name } returns "target"
            every { typeClass } returns typeOfClassUnderTest
        }
        every { assistantViewClass.name } returns "ClassTest"
        every { assistantViewClass.properties } returns listOf(targetProperty)
        every { fileManagerService.getFileFromProject(typeOfClassUnderTest, project) } returns fileContainingClassUnderTest

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, equalTo(fileContainingClassUnderTest))
    }

    @Test
    fun `test should return file under test from name when opening test suite`() {
        val fileContainingClassUnderTest = mock<AssistantViewFile>()
        every { assistantViewClass.name } returns "ClassTest"
        every { assistantViewClass.properties } returns emptyList()
        every { fileManagerService.findFileByName("Class", project) } returns fileContainingClassUnderTest

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, equalTo(fileContainingClassUnderTest))
    }

    @Test
    fun `test should handle test suite with no corresponding test file`() {
        every { assistantViewClass.name } returns "ClassTest"
        every { assistantViewClass.properties } returns emptyList()
        every { fileManagerService.findFileByName("Class", project) } returns null

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, absent())
    }
}
