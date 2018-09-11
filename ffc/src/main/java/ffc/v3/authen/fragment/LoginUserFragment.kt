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

package ffc.v3.authen.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.entity.Organization
import ffc.entity.gson.parseTo
import ffc.entity.gson.toJson
import ffc.v3.BuildConfig
import ffc.v3.R
import ffc.v3.android.onClick
import ffc.v3.android.onLongClick
import ffc.v3.util.assertionNotEmpty
import kotlinx.android.synthetic.main.fragment_login_user.btnBack
import kotlinx.android.synthetic.main.fragment_login_user.btnLogin
import kotlinx.android.synthetic.main.fragment_login_user.etPwd
import kotlinx.android.synthetic.main.fragment_login_user.etUsername
import kotlinx.android.synthetic.main.fragment_login_user.inputLayoutPassword
import kotlinx.android.synthetic.main.fragment_login_user.inputLayoutUsername
import kotlinx.android.synthetic.main.fragment_login_user.tvHospitalName
import org.jetbrains.anko.bundleOf

internal class LoginUserFragment : Fragment() {

    private val loginActivityListener: LoginActivityListener by lazy { activity as LoginActivityListener }

    lateinit var onLogin: (username: String, password: String) -> Unit
    var org: Organization?
        set(value) {
            arguments = bundleOf("organization" to value?.toJson())
        }
        get() = arguments?.getString("organization")?.parseTo()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnLogin.onClick {
            // Assert username and password
            val username = etUsername.text.toString()
            val password = etPwd.text.toString()
            val checkUsername = assertionNotEmpty(inputLayoutUsername, username,
                getString(R.string.no_username))
            val checkPwd = assertionNotEmpty(inputLayoutPassword, password,
                getString(R.string.no_password))
            // Login
            if (checkUsername && checkPwd) {
                loginActivityListener.onShowProgressBar(true)
                onLogin(username, password)
            }
        }
        btnBack.onClick { fragmentManager!!.popBackStack() }

        org!!.let {
            tvHospitalName.text = it.name
        }

        if (BuildConfig.DEBUG) {
            btnLogin.onLongClick {
                etUsername.setText("ploy")
                etPwd.setText("n")
                true
            }
        }
    }
}
