package ffc.android

import android.content.ActivityNotFoundException
import android.content.Context
import org.jetbrains.anko.browse

fun Context.browsePlayStore(appId: String): Boolean {
    return try {
        browse("market://details?id=$appId")
    } catch (anfe: ActivityNotFoundException) {
        browse("https://play.google.com/store/apps/details?id=" + appId)
    }
}
