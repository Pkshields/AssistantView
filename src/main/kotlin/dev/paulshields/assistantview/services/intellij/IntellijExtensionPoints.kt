package dev.paulshields.assistantview.services.intellij

import com.intellij.openapi.extensions.ExtensionPointName
import dev.paulshields.assistantview.lang.PairedFileFinder
import dev.paulshields.assistantview.lang.SourceFileInterpreter

class IntellijExtensionPoints {
    val sourceFileInterpreters = ExtensionPointName.create<SourceFileInterpreter>("dev.paulshields.assistantview.sourceFileInterpreter")
    val pairedFileFinders = ExtensionPointName.create<PairedFileFinder>("dev.paulshields.assistantview.pairedFileFinder")
}
