package ffc.app.util

fun String?.takeIfNotNullOrBlank(): String? = takeIf { !it.isNullOrBlank() }

fun <T> List<T>.takeIfNotEmpty() : List<T>? = takeIf { !it.isNullOrEmpty() }
