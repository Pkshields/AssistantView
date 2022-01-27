package dev.paulshields.assistantview.common

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.TimeUnit

class DispatcherTest {
    val target = Dispatcher()

    @Test
    fun `should run code block on background thread`() {
        val currentThreadId = Thread.currentThread().id
        var backgroundThreadId: Long? = null

        target.runOnBackgroundThread {
            backgroundThreadId = Thread.currentThread().id
        }

        await.atMost(1, TimeUnit.SECONDS).until { backgroundThreadId != null }
        assertThat(currentThreadId).isNotEqualTo(backgroundThreadId)
    }

    @Test
    fun `should queue code blocks and run them on the same thread`() {
        var firstCodeBlockThreadId: Long? = null
        var secondCodeBlockThreadId: Long? = null

        target.runOnBackgroundThread { firstCodeBlockThreadId = Thread.currentThread().id }
        target.runOnBackgroundThread { secondCodeBlockThreadId = Thread.currentThread().id }

        await.atMost(1, TimeUnit.SECONDS).until { firstCodeBlockThreadId != null && secondCodeBlockThreadId != null }
        assertThat(firstCodeBlockThreadId).isEqualTo(secondCodeBlockThreadId)
    }

    @Test
    fun `should dispose of dispatcher`() {
        target.dispose()

        assertThrows<Exception> {
            target.runOnBackgroundThread { println("Should not execute.") }
        }
    }
}
