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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ffc.entity.Organization
import ffc.entity.gson.toJson
import ffc.v3.BaseActivity
import ffc.v3.MapsActivity
import ffc.v3.R
import ffc.v3.authen.exception.LoginErrorException
import ffc.v3.authen.exception.LoginFailureException
import ffc.v3.authen.fragment.LoginActivityListener
import ffc.v3.authen.fragment.LoginOrgFragment
import ffc.v3.authen.fragment.LoginUserFragment
import ffc.v3.util.debug
import ffc.v3.util.gone
import ffc.v3.util.visible
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_login.ivCommunity
import kotlinx.android.synthetic.main.activity_login.ivOverlayBackground
import kotlinx.android.synthetic.main.activity_login.pbLoading
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity

class LoginActivity : BaseActivity(), LoginActivityListener, LoginPresenter {


    private val interactor: LoginInteractor by lazy {
        LoginInteractor().apply {
            loginPresenter = this@LoginActivity
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
                onLoginSuccess()
            } else {
                val fragment = LoginOrgFragment()
                fragment.orgSelected = { interactor.org = it }
                supportFragmentManager.beginTransaction()
                    .add(R.id.contentContainer, fragment, "LoginOrg")
                    .commit()
            }
        }
    }

    private fun initInstances() {
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

    override fun onLoginSuccess() {
        onShowProgressBar(false)
        startActivity<MapsActivity>()
        finish()
    }

    override fun onError(throwable: Throwable) {
        onShowProgressBar(false)
        val message = when (throwable) {
            is LoginErrorException -> getString(R.string.identification_error)
            is LoginFailureException -> throwable.message
            else -> "Something wrong"
        }
        message?.let { longToast(it) }
    }

    override fun onOrgSelected(org: Organization) {
        val fragment = LoginUserFragment()
        fragment.onLogin = { user, pass -> interactor.doLogin(user, pass) }
        fragment.arguments = bundleOf("organization" to org.toJson())

        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

}
