package ffc.android

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.RawRes
//import android.support.annotation.RawRes
import com.google.gson.Gson
import ffc.entity.gson.ffcGson
import java.io.BufferedReader
import java.io.InputStreamReader

inline fun <reified T> Context.rawAs(@RawRes rawId: Int, gson: Gson = ffcGson): T {
    val reader = BufferedReader(InputStreamReader(resources.openRawResource(rawId)))
    return gson.fromJson(reader.readText(), T::class.java)
}

fun Context.isXLargeTablet(context: Context) = context.resources.configuration.screenLayout and
    Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
