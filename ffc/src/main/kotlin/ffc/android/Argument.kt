package ffc.android

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import ffc.entity.gson.ffcGson
import ffc.entity.gson.parseTo

inline fun <reified T> Intent.getExtra(key: String, gson: Gson = ffcGson): T? {
    return getStringExtra(key)?.parseTo<T>(gson)
}

inline fun <reified T> Bundle.getAs(key: String, gson: Gson = ffcGson): T? {
    return getString(key)?.parseTo<T>(gson)
}
