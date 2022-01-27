package dev.paulshields.assistantview.common

import com.intellij.openapi.Disposable
import java.util.concurrent.Executors

/**
 * Runs code block on a single background thread.
 *
 * Workaround class due to coroutine issues in intellij plugins: https://youtrack.jetbrains.com/issue/IDEA-277886
 * Until that is fixed, back to Java threads.
 */
class Dispatcher : Disposable {
    private val executor = Executors.newSingleThreadExecutor()

    fun runOnBackgroundThread(block: () -> Unit) = executor.execute(block)

    override fun dispose() {
        executor.shutdownNow()
    }
}
