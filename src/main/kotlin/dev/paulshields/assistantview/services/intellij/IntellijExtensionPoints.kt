package dev.paulshields.assistantview.services.intellij

import com.intellij.openapi.extensions.ExtensionPointName
import dev.paulshields.assistantview.lang.parser.SourceFileParser

class IntellijExtensionPoints {
    val sourceFileParsers = ExtensionPointName.create<SourceFileParser>("dev.paulshields.assistantview.sourceFileParser")
}
