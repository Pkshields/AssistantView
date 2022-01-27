package dev.paulshields.assistantview.extensions

import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runReadAction

fun <T> runWithReadPermission(block: () -> T) = runReadAction(block)

fun runOnUiThread(block: () -> Unit) = invokeLater(runnable = block)
