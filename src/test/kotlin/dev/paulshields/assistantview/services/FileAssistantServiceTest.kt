package dev.paulshields.assistantview.services

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import dev.paulshields.assistantview.sourcefiles.AssistantViewClass
import dev.paulshields.assistantview.sourcefiles.AssistantViewFile
import dev.paulshields.assistantview.testcommon.mock
import io.mockk.every
import org.junit.jupiter.api.Test

class FileAssistantServiceTest {
    private val fileManagerService = mock<FileManagerService>()
    private val file = mock<AssistantViewFile>().apply {
        every { mainClass?.superClass } returns null
        every { mainClass?.interfaces } returns emptyList()
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
    fun `should return null if no counterpart class available`() {
        val result = target.getCounterpartFile(file)

        assertThat(result).isNull()
    }
}
