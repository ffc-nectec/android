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

package ffc.v3.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ffc.v3.BaseActivity
import ffc.v3.MapsActivity
import ffc.v3.R
import ffc.v3.authen.LoginInteractor
import ffc.v3.authen.LoginPresenter
import ffc.v3.fragment.LoginOrgFragment
import ffc.v3.util.EventListener
import ffc.v3.util.gone
import ffc.v3.util.visible
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor

class LoginActivity : BaseActivity(), LoginPresenter, EventListener {

    //    lateinit var ivOverlayBackground: ImageView
//    lateinit var pbLoading: ProgressBar
    lateinit var interactor: LoginInteractor
//    lateinit var onLoginRequest: (username: String, password: String) -> Unit
//    lateinit var onNext: ((Organization) -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Blur background image
        blurImage()
        initInstances()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
//                .add(R.id.contentContainer, getOrgFragment(), "LoginOrg")
                .add(R.id.contentContainer, LoginOrgFragment(), "LoginOrg")
                .commit()
        }

//        interactor = LoginInteractor(this)
//        onLoginRequest = { username, password ->
//            interactor.doLogin(username, password)
//        }
//
//        onNext = {
//            interactor.org = it
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, getUserPassFragment(org))
//                .addToBackStack(null)
//                .commit()
//        }
    }

    private fun initInstances() {

    }

    private fun blurImage() {
        Glide.with(this)
            .load(R.drawable.community)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(4, 3)))
            .into(ivCommunity)
    }

//    private fun getOrgFragment(): Fragment {
//        return LoginOrgFragment().apply {
//
//        }
//    }

//    private fun getUserPassFragment(org: Organization): Fragment {
//        return LoginUserFragment().apply{
//            organization = org
//        }
//    }

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
        startActivity(intentFor<MapsActivity>())
    }

    override fun onError(message: Int) {
    }

    override fun onError(message: String) {
    }

}
