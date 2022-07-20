package dev.paulshields.assistantview.extensions

import com.jetbrains.cidr.lang.psi.impl.OCFileImpl
import com.jetbrains.cidr.lang.symbols.cpp.OCFunctionSymbol
import com.jetbrains.cidr.lang.symbols.cpp.OCStructSymbol

val OCFileImpl.classes
    get() = symbolTable?.contents?.filterIsInstance<OCStructSymbol>().orEmpty()

val OCFileImpl.topLevelFunctions
    get() = symbolTable?.contents?.filterIsInstance<OCFunctionSymbol>().orEmpty()
