package dev.paulshields.assistantview

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.project.Project
import dev.paulshields.assistantview.factories.CodeEditorFactory
import dev.paulshields.assistantview.factories.DocumentFactory
import dev.paulshields.assistantview.intellijwrappers.FilenameIndex
import dev.paulshields.assistantview.intellijwrappers.PsiTypesUtil
import dev.paulshields.assistantview.services.AssistantViewService
import dev.paulshields.assistantview.services.FileAssistantService
import dev.paulshields.assistantview.services.FileManagerService
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DependencyInjectionListener : AppLifecycleListener {
    private val module = module {
        single { FileAssistantService(get()) }
        single { FileManagerService(get(), get()) }
        single { AssistantViewService() }
        single { DocumentFactory() }
        single { CodeEditorFactory(get()) }
        single { FilenameIndex() }
        single { PsiTypesUtil() }
    }

    override fun appStarting(projectFromCommandLine: Project?) {
        super.appStarting(projectFromCommandLine)

        startKoin {
            modules(module)
        }
    }
}
