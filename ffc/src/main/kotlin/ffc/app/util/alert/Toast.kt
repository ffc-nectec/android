package ffc.app.util.alert

import android.widget.Toast
import ffc.api.ApiErrorException
import ffc.app.BuildConfig
import ffc.app.FamilyFolderActivity
import ffc.app.auth.relogin

fun FamilyFolderActivity.handle(throwable: Throwable, fallbackMessage: String? = null) {
    if (BuildConfig.DEBUG)
        throwable.printStackTrace()

    when (throwable) {
        is ApiErrorException -> handleApiError(throwable)
        else -> Toast.makeText(
            this,
            throwable.message ?: fallbackMessage ?: throwable.toString(),
            Toast.LENGTH_SHORT).show()
    }
}

private fun FamilyFolderActivity.handleApiError(throwable: ApiErrorException) {
    when (throwable.code) {
        401 -> relogin()
        else -> Toast.makeText(
            this,
            throwable.message ?: throwable.toString(),
            Toast.LENGTH_SHORT).show()
    }
}

fun FamilyFolderActivity.toast(message: String) {
    if (BuildConfig.DEBUG)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
