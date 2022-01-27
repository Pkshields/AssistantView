package dev.paulshields.assistantview

import com.intellij.ide.AppLifecycleListener
import dev.paulshields.assistantview.common.Dispatcher
import dev.paulshields.assistantview.factories.AssistantViewFactory
import dev.paulshields.assistantview.factories.CodeEditorDocumentFactory
import dev.paulshields.assistantview.factories.CodeEditorFactory
import dev.paulshields.assistantview.factories.ToolWindowUIFactory
import dev.paulshields.assistantview.factories.intellij.IntellijSingletons
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import dev.paulshields.assistantview.services.intellij.IntellijFileSystemService
import dev.paulshields.lok.logInfo
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DependencyInjector : AppLifecycleListener {
    private val module = module {
        single { Dispatcher() }

        single { IntellijFileSystemService() }
        single { FileManagerService(get()) }
        single { FileAssistantService(get()) }
        single { AssistantViewService() }

        single { IntellijSingletons() }
        single { CodeEditorDocumentFactory() }
        single { CodeEditorFactory(get(), get()) }
        single { ToolWindowUIFactory(get()) }
        single { AssistantViewFactory(get(), get()) }
    }

    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        logInfo { "Hello from AssistantView. Injecting dependencies..." }

        startKoin {
            modules(module)
        }
    }
}
