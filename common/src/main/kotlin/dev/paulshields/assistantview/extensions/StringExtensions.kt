package dev.paulshields.assistantview.extensions

fun String.lastIndexOfOrNull(needle: Char) =
    this.lastIndexOf(needle).let { if (it != -1) it else null }
