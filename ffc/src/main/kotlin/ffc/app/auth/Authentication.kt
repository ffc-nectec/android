/*
 * Copyright (c) 2019 NECTEC
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

package ffc.app.auth

import android.content.Context
//import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import ffc.android.get
import ffc.android.put
import ffc.app.BuildConfig
import ffc.app.util.Analytics
import ffc.entity.Organization
import ffc.entity.User
import ffc.entity.gson.toJson
import org.jetbrains.anko.defaultSharedPreferences

interface Authentication {

    var org: Organization?

    var token: String?

    var user: User?

    val isLoggedIn: Boolean
        get() = org != null && user != null

    fun clear()
}

private class PreferenceAuthen(context: Context) : Authentication {

    var preference = context.defaultSharedPreferences

    override var org: Organization?
        get() = preference.get<Organization>("org")
        set(value) {
            preference.edit().put("org", value).apply()
        }

    override var token: String?
        get() = preference.getString("token", null)
        set(value) {
            preference.edit().putString("token", value).apply()
        }

    override var user: User?
        get() = preference.get<User>("user")
        set(value) {
            preference.edit().put("user", value).apply()
            if (value != null) {
                //Firebase Anonymously sign for Upload Photo to FB's Storage
                FirebaseAuth.getInstance().signInAnonymously()
                if (BuildConfig.BUILD_TYPE == "release") {
//                    Crashlytics.setUserIdentifier(value.id)
//                    Crashlytics.setUserName(value.name)
                    Analytics.setUserProperty(org, user)
                }
            } else {
                FirebaseAuth.getInstance().signOut()
                if (BuildConfig.BUILD_TYPE == "release") {
//                    Crashlytics.setUserIdentifier(null)
//                    Crashlytics.setUserName(null)
                    Analytics.setUserProperty(null, null)
                }
            }
        }

    override fun clear() {
        user = null
        preference.edit().clear().apply()
    }

    override fun toString(): String {
        return "org = ${org?.toJson()}\nuser = ${user?.toJson()}\ntoken = $token"
    }
}

fun auth(context: Context): Authentication = PreferenceAuthen(context)
