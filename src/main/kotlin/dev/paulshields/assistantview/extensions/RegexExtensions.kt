package dev.paulshields.assistantview.extensions

fun Regex.extractGroups(input: String) =
    this.find(input)
        ?.groups
        ?.drop(1)
        ?.mapNotNull { it?.value }
        ?: emptyList()
