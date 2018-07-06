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

package ffc.v3

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import ffc.entity.Organization
import ffc.v3.util.org
import org.jetbrains.anko.contentView
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.design.indefiniteSnackbar

open class BaseActivity : AppCompatActivity() {

    var offlineSnackbar: Snackbar? = null
    var isOnline = false
        private set(value) {
            field = value
            onConnectivityChanged(field)
        }

    val org: Organization get() = defaultSharedPreferences.org!!

    private val connectivityChange by lazy { ConnectivityChangeReceiver { isOnline = it } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isOnline = connectivityManager.isConnectedOrConnecting
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityChange, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityChange)
    }

    protected open fun onConnectivityChanged(
        isConnect: Boolean,
        message: String = "You are offline"
    ) {
        if (isConnect) {
            offlineSnackbar?.dismiss()
        } else {
            contentView?.let {
                offlineSnackbar = indefiniteSnackbar(it, message)
            }
        }
    }
}
