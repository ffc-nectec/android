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

package ffc.app

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
//import android.support.annotation.LayoutRes
//import android.support.design.widget.Snackbar
//import android.support.v4.app.Fragment
//import android.support.v7.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ffc.android.connectivityManager
import ffc.android.onClick
import ffc.app.auth.auth
import ffc.entity.Organization
import ffc.entity.User
import org.jetbrains.anko.contentView
import org.jetbrains.anko.design.indefiniteSnackbar

//import org.jetbrains.anko.contentView
//import org.jetbrains.anko.design.indefiniteSnackbar


@SuppressLint("Registered")
open class FamilyFolderActivity : AppCompatActivity() {

    var offlineSnackbar: Snackbar? = null
    var isOnline = false
        private set(value) {
            field = value
            onConnectivityChanged(field)
        }

    val org: Organization?
        get() = auth(this).org ?: devOrg
    val currentUser: User?
        get() = auth(this).user ?: devUser
    private val devOrg = if (isDev) Organization() else null
    private val devUser = if (isDev) User().apply { orgId = devOrg?.id } else null

    protected var savedInstanceState: Bundle? = null
    private val connectivityChange by lazy { ConnectivityChangeReceiver { isOnline = it } }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar)
        if (toolbar != null && toolbar is Toolbar) {
            setSupportActionBar(toolbar)
        }
    }

    fun onToolbarClick(block: (Toolbar) -> Unit) {
        findViewById<Toolbar>(R.id.toolbar).onClick(block)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isOnline = connectivityManager.isConnected
        this.savedInstanceState = savedInstanceState
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
        message: String = getString(R.string.you_offline)
    ) {
        if (isConnect) {
            offlineSnackbar?.dismiss()
        } else {
            contentView?.let {
                //offlineSnackbar =  indefiniteSnackbar(it, message)
            }
        }
    }
}

val Fragment.familyFolderActivity: FamilyFolderActivity
    get() = activity as FamilyFolderActivity
