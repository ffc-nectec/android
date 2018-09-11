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

package ffc.v3.messaging

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import ffc.v3.android.tag
import ffc.v3.api.FfcCentral
import ffc.v3.authen.getIdentityRepo
import org.jetbrains.anko.defaultSharedPreferences
import retrofit2.dsl.enqueue
import java.lang.IllegalArgumentException

internal class FirebaseMessaging(val application: Application) : Messaging {

    private val service by lazy { FfcCentral().service<TokenService>() }

    private val preferences by lazy { application.defaultSharedPreferences }

    private val org by lazy { getIdentityRepo(application).org }

    override fun subscripbe(token: String?) {
        try {
            val newToken = if (token == null) preferences.tempToken else token
            require(newToken != null)
            if (preferences.lastToken != null) {
                unsubscribe()
            }
            check(org != null)
            service.updateToken(org!!.id, mapOf("firebaseToken" to token!!)).enqueue {
                always { Log.d(tag, "Register token $token") }
                onSuccess { preferences.lastToken = token }
            }
        } catch (ex: IllegalStateException) {
            preferences.tempToken = token
        } catch (ex: IllegalArgumentException) {
            preferences.tempToken = token
        }
    }

    override fun unsubscribe() {
        try {
            val preferences = application.defaultSharedPreferences
            val token = preferences.lastToken
            check(org != null)
            check(token != null)

            service.removeToken(org!!.id, token!!).enqueue {
                if (preferences.lastToken == token) //lastToken may change before this call
                    preferences.lastToken = null
            }
        } catch (ex: IllegalStateException) {
            //Do nothing
        }
    }
}

private var SharedPreferences.lastToken: String?
    set(value) = edit().putString("lastToken", value).apply()
    get() = getString("lastToken", null)

private var SharedPreferences.tempToken: String?
    set(value) = edit().putString("tempToken", value).apply()
    get() = getString("tempToken", null)
