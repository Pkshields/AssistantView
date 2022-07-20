package dev.paulshields.assistantview.extensions

fun <T : Any?, R : Any> Iterable<T>.flatMapNotNull(transform: (T) -> List<R>?) = mapNotNull(transform).flatten()
