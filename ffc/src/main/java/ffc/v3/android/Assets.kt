/*
 * Copyright (c) 2018 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ffc.v3.android

import android.content.Context
import com.google.gson.Gson
import ffc.entity.gson.ffcGson
import ffc.entity.gson.parseTo
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Throws(IOException::class)
fun Context.assetAsString(filename: String): String {
    val reader = BufferedReader(InputStreamReader(getAssets().open(filename)))
    return reader.readText()
}

inline fun <reified T> Context.assetAs(filename: String, gson: Gson = ffcGson): T {
    return assetAsString(filename).parseTo(ffcGson)
}
