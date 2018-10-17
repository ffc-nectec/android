package ffc.android

import android.widget.Toast
import ffc.app.BuildConfig
import ffc.app.FamilyFolderActivity

fun FamilyFolderActivity.toast(throwable: Throwable, fallbackMessage: String? = null) {
    if (BuildConfig.DEBUG)
        throwable.printStackTrace()

    Toast.makeText(
        this,
        throwable.message ?: fallbackMessage ?: throwable.toString(),
        Toast.LENGTH_SHORT).show()
}

fun FamilyFolderActivity.toast(message: String) {
    if (BuildConfig.DEBUG)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
