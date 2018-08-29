package ffc.v3.android

import android.app.Activity
import android.widget.Toast
import ffc.v3.BuildConfig

fun Activity.toast(throwable: Throwable, fallbackMessage: String? = null) {
    if (BuildConfig.DEBUG)
        throwable.printStackTrace()
    Toast.makeText(
        this,
        throwable.message ?: fallbackMessage ?: throwable.toString(),
        Toast.LENGTH_SHORT).show()
}
