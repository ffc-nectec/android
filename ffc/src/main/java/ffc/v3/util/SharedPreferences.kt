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
package ffc.v3.util

import android.content.SharedPreferences
import com.google.gson.Gson
import ffc.v3.Org

inline fun <reified T> SharedPreferences.get(key: String, gson: Gson = defaultGson): T? =
  this.getString(key, null)?.parseTo<T>(gson)

var SharedPreferences.org: Org?
  set(value) = edit().put("org", value).apply()
  get() = get("org")

var SharedPreferences.firebaseToken: String?
  set(value) = edit().putString("firebaseToken", value).apply()
  get() = getString("firebaseToken", null)

fun <T> SharedPreferences.Editor.put(
  key: String,
  value: T,
  gson: Gson = defaultGson
) = this.apply { putString(key, value!!.toJson(gson)) }



