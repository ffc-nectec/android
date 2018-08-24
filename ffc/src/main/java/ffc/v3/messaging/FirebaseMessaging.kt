package ffc.v3.messaging

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import ffc.v3.android.tag
import ffc.v3.api.FfcCentral
import ffc.v3.authen.getIdentityRepo
import org.jetbrains.anko.defaultSharedPreferences
import retrofit2.dsl.enqueue

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
            require(org != null)
            service.updateToken(org!!.id, mapOf("firebaseToken" to token!!)).enqueue {
                always { Log.d(tag, "Register token $token") }
                onSuccess { preferences.lastToken = token }
            }
        } catch (ex: IllegalStateException) {
            preferences.tempToken = token
        }
    }

    override fun unsubscribe() {
        try {
            val preferences = application.defaultSharedPreferences
            val token = preferences.lastToken
            require(org != null)
            require(token != null)

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
