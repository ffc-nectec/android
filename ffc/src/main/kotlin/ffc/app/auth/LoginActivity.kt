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

import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import ffc.android.allowTransitionOverlap
import ffc.android.enter
import ffc.android.exit
import ffc.android.find
import ffc.android.findFirst
import ffc.android.onLongClick
import ffc.android.setTransition
import ffc.app.BuildConfig
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.auth.legal.LegalAgreementActivity
import ffc.app.setting.SettingsActivity
import ffc.entity.Organization
import ffc.entity.gson.parseTo
import ffc.entity.gson.toJson
import kotlinx.android.synthetic.main.login_activity.versionView
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.startActivity
import timber.log.Timber

class LoginActivity : FamilyFolderActivity(), LoginPresenter {

    private lateinit var interactor: LoginInteractor

    val isRelogin
        get() = intent.getBooleanExtra("relogin", false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        setTransition {
            enterTransition = Fade().enter().addTarget(R.id.overlay)
        }

        val auth = auth(this)
        Timber.d("User id = ${auth.user?.id}")

        interactor = LoginInteractor(this, auth, isRelogin)

        savedInstanceState?.let { saved ->
            val org = saved.getString("org")?.parseTo<Organization>()
            org?.let { interactor.org = org }
        }
        versionView.text = "v${BuildConfig.VERSION_NAME}"
        versionView.onLongClick {
            startActivity<SettingsActivity>()
            true
        }
    }

    override fun showOrgSelector() {
        if (savedInstanceState == null) {
            val fragment = LoginOrgFragment()
            fragment.orgSelected = { interactor.org = it }
            fragment.setTransition {
                exitTransition = Explode().exit()
                allowTransitionOverlap = false
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, fragment, "Org")
                .commit()
        }
    }

    override fun onLoginSuccess() {
        if (!isRelogin) {
            startActivity<LegalAgreementActivity>()
            overridePendingTransition(0, 0)
        }
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        interactor.org?.let { outState.putString("org", it.toJson()) }
        super.onSaveInstanceState(outState)
    }

    override fun onError(throwable: Throwable) {
        supportFragmentManager
            .findFirst<LoginExceptionPresenter>("otp", "Login", "Org")?.onException(throwable)
    }

    override fun onOrgSelected(org: Organization) {
        val userPassFragment = supportFragmentManager.find("Login")
            ?: LoginUserFragment()
        userPassFragment.onLogin = { user, pass -> interactor.doLogin(user, pass) }
        userPassFragment.org = org
        userPassFragment.setTransition {
            enterTransition = Slide(Gravity.END).enter(delay = 50)
            exitTransition = Explode().exit()
        }
        val orgFragment = supportFragmentManager.find<LoginOrgFragment?>("Org")

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.contentContainer, userPassFragment, "Login")
                if (orgFragment != null) {
                    //there no orgFragment for Relogin action
                    hide(orgFragment)
                    addToBackStack(null)
                }
            }.commit()
        }
    }

    override fun onActivateRequire() {
        val otpFragment = supportFragmentManager.find("otp") ?: OtpFragment()
        otpFragment.onOtpSend = { interactor.doActivate(it) }
        otpFragment.setTransition {
            enterTransition = Slide(Gravity.END).enter(delay = 50)
        }

        val userPassFragment = supportFragmentManager.find<LoginUserFragment?>("Login")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.contentContainer, otpFragment, "otp")
                if (userPassFragment != null) {
                    //there no orgFragment for Relogin action
                    hide(userPassFragment)
                    addToBackStack(null)
                }
            }.commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            super.onBackPressed()
        else {
//            alert(Appcompat, R.string.r_u_sure_to_close_app, R.string.close_app) {
//                iconResource = R.drawable.ic_logout_color_24dp
//                positiveButton(R.string.close_app) {
//                    finishAffinity()
//                    if (isRelogin) auth(this@LoginActivity).clear()
//                }
//                negativeButton(R.string.no) {}
//            }.show()
        }
    }
}

fun FamilyFolderActivity.relogin() {
    startActivity<LoginActivity>("relogin" to true)
}
