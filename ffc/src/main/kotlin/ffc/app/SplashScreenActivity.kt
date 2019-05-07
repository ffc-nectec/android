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

import android.os.Bundle
import android.os.Handler
import ffc.android.browsePlayStore
import ffc.android.observe
import ffc.android.onLongClick
import ffc.android.sceneTransition
import ffc.android.viewModel
import ffc.api.FfcCentral
import ffc.app.auth.LoginActivity
import ffc.app.auth.auth
import ffc.app.auth.legal.LegalAgreementActivity
import ffc.app.setting.SettingsActivity
import ffc.app.util.SimpleViewModel
import ffc.app.util.version.Version
import ffc.app.util.version.versionCheck
import kotlinx.android.synthetic.main.splash_screen_activity.appLogo
import kotlinx.android.synthetic.main.splash_screen_activity.versionView
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import timber.log.Timber

class SplashScreenActivity : FamilyFolderActivity() {

    val viewModel by lazy { viewModel<SimpleViewModel<Version>>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_activity)

        Handler().postDelayed({
            versionCheck().checkForUpdate {
                onFound { viewModel.content.value = it }
                onNotFound { viewModel.content.value = null }
                onFail {
                    viewModel.content.value = null
                    viewModel.exception.value = it
                }
            }
        }, 1700)

        observe(viewModel.content) {
            if (it != null) {
                //Show update dialog
                alert("พบเวอร์ชั่นใหม่ (v$it) กรุณาทำการอัพเดทก่อนเข้าใช้งาน", getString(R.string.update)) {
                    isCancelable = false
                    positiveButton(R.string.update) {
                        browsePlayStore(BuildConfig.APPLICATION_ID)
                        finishAffinity()
                    }
                    negativeButton(R.string.close_app) { finishAffinity() }
                }.show()
            } else {
                //Already up to date
                gotoNextActivity()
            }
        }
        observe(viewModel.exception) {
            Timber.i(it, "Can't check latest release version")
        }

        versionView.text = "v${BuildConfig.VERSION_NAME}"
        versionView.onLongClick {
            startActivity<SettingsActivity>()
            true
        }
    }

    private fun gotoNextActivity() {
        val auth = auth(this)
        if (auth.isLoggedIn) {
            FfcCentral.token = auth.token
            startActivity<LegalAgreementActivity>()
            overridePendingTransition(0, 0)
        } else {
            startActivity(intentFor<LoginActivity>(), sceneTransition(
                appLogo to getString(R.string.transition_app_logo)
            ))
        }
        finish()
    }
}
