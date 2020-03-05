package dev.paulshields.assistantview

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DependencyInjectionListener : AppLifecycleListener {

    private val module = module {
        single { FileAssistantService(get()) }
        single { FileManagerService() }
    }

    override fun appStarting(projectFromCommandLine: Project?) {
        super.appStarting(projectFromCommandLine)

        startKoin {
            modules(module)
        }
    }
}
