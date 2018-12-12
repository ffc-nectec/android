package ffc.app.util

import android.widget.EditText

fun String?.takeIfNotNullOrBlank(): String? = takeIf { !it.isNullOrBlank() }

fun String?.setInto(editText: EditText) = takeIfNotNullOrBlank()?.let { editText.setText(it) }

fun <T> List<T>.takeIfNotEmpty(): List<T>? = takeIf { !it.isNullOrEmpty() }
