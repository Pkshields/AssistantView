package dev.paulshields.assistantview

import com.intellij.ide.AppLifecycleListener
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import dev.paulshields.lok.logInfo
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DependencyInjector : AppLifecycleListener {
    private val module = module {
        single { FileManagerService() }
        single { FileAssistantService(get()) }
    }

    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        logInfo { "Hello from AssistantView. Injecting dependencies..." }

        startKoin {
            modules(module)
        }
    }
}
