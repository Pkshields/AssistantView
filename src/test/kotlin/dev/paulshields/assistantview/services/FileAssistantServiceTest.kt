package dev.paulshields.assistantview.services

import com.intellij.openapi.project.Project
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import org.junit.Test

class FileAssistantServiceTest {

    private val fileManagerService = mock<FileManagerService>()

    private val project = mock<Project>()
    private val assistantViewClass = mock<AssistantViewClass>()
    private val assistantViewFile = mock<AssistantViewFile> {
        on { mainClass } doReturn assistantViewClass
    }

    private val target = FileAssistantService(fileManagerService)

    @Test
    fun `test should return file containing counterpart base class`() {
        val fileContainingBaseClass = mock<AssistantViewFile>()
        val baseClass = mock<AssistantViewClass>()
        whenever(assistantViewClass.baseClass).thenReturn(baseClass)
        whenever(fileManagerService.getFileFromProject(baseClass, project)).thenReturn(fileContainingBaseClass)

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, equalTo(fileContainingBaseClass))
    }

    @Test
    fun `test should return file containing first interface if no base class`() {
        val fileContainingInterface = mock<AssistantViewFile>()
        val firstInterface = mock<AssistantViewClass>()
        val secondInterface = mock<AssistantViewClass>()
        whenever(assistantViewClass.interfaces).thenReturn(listOf(firstInterface, secondInterface))
        whenever(fileManagerService.getFileFromProject(firstInterface, project)).thenReturn(fileContainingInterface)

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, equalTo(fileContainingInterface))
    }

    @Test
    fun `test should handle file with no classes in it`() {
        whenever(assistantViewFile.mainClass).thenReturn(null)

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, absent())
    }

    @Test
    fun `test should handle class with no base class or interfaces`() {
        whenever(assistantViewFile.mainClass).thenReturn(assistantViewClass)
        whenever(assistantViewClass.baseClass).thenReturn(null)
        whenever(assistantViewClass.interfaces).thenReturn(emptyList())

        val result = target.getCounterpartFile(assistantViewFile, project)

        assertThat(result, absent())
    }
}
