package dev.paulshields.assistantview.extensions

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project

val Project.isDumb
    get() = DumbService.getInstance(this).isDumb
