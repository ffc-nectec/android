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

package ffc.v3.authen.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ffc.v3.BaseActivity
import ffc.v3.MapsActivity
import ffc.v3.R
import ffc.v3.authen.LoginInteractor
import ffc.v3.authen.fragment.LoginOrgFragment
import ffc.v3.authen.getIdentityRepo
import ffc.v3.util.LoginEventListener
import ffc.v3.util.debug
import ffc.v3.util.gone
import ffc.v3.util.visible
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_login.ivCommunity
import kotlinx.android.synthetic.main.activity_login.ivOverlayBackground
import kotlinx.android.synthetic.main.activity_login.pbLoading
import org.jetbrains.anko.startActivity

class LoginActivity : BaseActivity(), LoginEventListener {

    val interactor: LoginInteractor by lazy {
        LoginInteractor().apply {
            idRepo = getIdentityRepo(this@LoginActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initInstances()

        if (savedInstanceState == null) {
            debug(interactor.idRepo.toString())
            if (interactor.isLoggedIn) {
                startActivity<MapsActivity>()
                finish()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.contentContainer, LoginOrgFragment(), "LoginOrg")
                    .commit()
            }
        }
    }

    private fun initInstances() {
        // Blur background image
        blurImage()
        onShowProgressBar(true)
    }

    private fun blurImage() {
        Glide.with(this)
            .load(R.drawable.community)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(4, 3)))
            .into(ivCommunity)
    }

    override fun onShowProgressBar(state: Boolean) {
        if (state) {
            ivOverlayBackground.visible()
            pbLoading.visible()
        } else {
            ivOverlayBackground.gone()
            pbLoading.gone()
        }
    }

    override fun onLoginActivity() {

    }
}