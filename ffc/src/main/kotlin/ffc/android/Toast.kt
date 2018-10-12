package ffc.android

import android.app.Activity
import android.widget.Toast
import ffc.app.BuildConfig

fun Activity.toast(throwable: Throwable, fallbackMessage: String? = null) {
    if (BuildConfig.DEBUG)
        throwable.printStackTrace()
    Toast.makeText(
        this,
        throwable.message ?: fallbackMessage ?: throwable.toString(),
        Toast.LENGTH_SHORT).show()
}

fun Activity.toast(message: String) {
    if (BuildConfig.DEBUG)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
