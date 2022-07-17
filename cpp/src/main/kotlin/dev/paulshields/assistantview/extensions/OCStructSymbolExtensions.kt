package dev.paulshields.assistantview.extensions

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.lang.symbols.OCResolveContext
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol
import com.jetbrains.cidr.lang.types.OCReferenceTypeSimple

fun OCStructSymbol.getSuperClasses(project: Project) =
    getContainingPsiFile(project)?.let { containingFile ->
        val resolveContext = OCResolveContext.forPsi(containingFile)

        getBaseCppClasses(containingFile)
            .filterIsInstance<OCReferenceTypeSimple>()
            .flatMap { getStructSymbolFromReferenceType(it, resolveContext) }
    } ?: emptyList()

private fun getStructSymbolFromReferenceType(referenceType: OCReferenceTypeSimple, resolveContext: OCResolveContext) =
    referenceType
        .reference
        .resolveToSymbols(resolveContext)
        .filterIsInstance<OCStructSymbol>()
