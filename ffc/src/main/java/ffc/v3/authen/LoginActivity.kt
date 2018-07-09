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

package ffc.v3.authen

import android.os.Bundle
import android.support.v4.app.Fragment
import ffc.entity.Organization
import ffc.entity.Token
import ffc.v3.BaseActivity
import ffc.v3.MapsActivity
import ffc.v3.R
import ffc.v3.api.FfcCentral
import ffc.v3.api.OrgService
import ffc.v3.util.debugToast
import ffc.v3.util.get
import me.piruin.spinney.Spinney
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.intentFor

class LoginActivity : BaseActivity(), LoginPresenter {

    private val organization by lazy { findViewById<Spinney<Organization>>(R.id.org) }
    private val orgService = FfcCentral().service<OrgService>()

    lateinit var controller: LoginController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val authorize = defaultSharedPreferences.get<Token>("token")
        if (authorize?.isNotExpire == true) {
            debugToast("Use last token")
            FfcCentral.TOKEN = authorize.token.toString()
            startActivity(intentFor<MapsActivity>())
            finish()
            return
        }

        var controller = LoginController(this)
        controller.presenter = this

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, getOrgFragmentt(), "org")
            .addToBackStack(null)
            .commit()

        controller.requestMyOrg {
            val fragment = supportFragmentManager.findFragmentByTag("tag") as OrgSelectFragment
        }
    }

    private fun getOrgFragmentt(): Fragment {
        return OrgSelectFragment().apply {
            onNext = {
                controller.org = it
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, getUserPassFragment(org))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun getUserPassFragment(org: Organization): Fragment {
        return UserPassFragment().apply {
            organization = org
            onLoginRequest = { user, pass ->
                controller.doLogin(user, pass)
            }
        }
    }

    override fun onLoginSuccess(callback: () -> Unit) {
        startActivity(intentFor<MapsActivity>())
    }

    override fun error(message: String) {
    }
}
