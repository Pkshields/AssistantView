package dev.paulshields.assistantview.lang

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.lang.psi.impl.OCFileImpl
import com.jetbrains.cidr.lang.symbols.cpp.OCFunctionSymbol
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol
import dev.paulshields.assistantview.extensions.classes
import dev.paulshields.assistantview.extensions.flatMapNotNull
import dev.paulshields.assistantview.extensions.topLevelFunctions

class CppPairedFileFinder : PairedFileFinder {
    override fun findPairedFile(file: AssistantViewFile): AssistantViewFile? {
        if (file !is CppAssistantViewFile) return null

        val methods = findAllMethodsInOcFile(file.ocFile)
        return getFilesPairedToMethods(methods, file.project).firstOrNull()
    }

    private fun findAllMethodsInOcFile(ocFile: OCFileImpl): List<OCFunctionSymbol> {
        val methodsInClasses = ocFile
            .classes
            .flatMapNotNull(::getMethodsInClass)

        return methodsInClasses + ocFile.topLevelFunctions
    }

    private fun getMethodsInClass(clazz: OCStructSymbol) = clazz.membersList?.filterIsInstance<OCFunctionSymbol>()

    /**
     * if the function is a predeclaration, this will return the file that includes the declaration.
     * If the function is the declaration and it includes a predeclaration in a header file, it will return that header file.
     * If the function is the declaration and it does not include a predeclaration, it will return null.
     */
    private fun getFilesPairedToMethods(functionsInFile: List<OCFunctionSymbol>, project: Project) =
        functionsInFile
            .mapNotNull { it.getAssociatedSymbol(project) }
            .mapNotNull { it.getContainingOCFile(project) as? OCFileImpl }
            .map { CppAssistantViewFile(it, project) }
}
