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

package ffc.app.auth.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.check
import ffc.android.isNotBlank
import ffc.android.onClick
import ffc.android.onLongClick
import ffc.app.R
import ffc.app.dev
import ffc.entity.Organization
import ffc.entity.gson.parseTo
import ffc.entity.gson.toJson
import kotlinx.android.synthetic.main.fragment_login_user.btnBack
import kotlinx.android.synthetic.main.fragment_login_user.btnLogin
import kotlinx.android.synthetic.main.fragment_login_user.etPwd
import kotlinx.android.synthetic.main.fragment_login_user.etUsername
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
            try {
                etUsername.check {
                    that { isNotBlank }
                    message = getString(R.string.no_username)
                }
                etPwd.check {
                    that { isNotBlank }
                    message = getString(R.string.no_password)
                }
                val username = etUsername.text.toString().trim()
                val password = etPwd.text.toString().trim()

                loginActivityListener.onShowProgressBar(true)
                onLogin(username, password)
            } catch (handled: IllegalStateException) {
            }
        }
        btnBack.onClick { fragmentManager!!.popBackStack() }

        org!!.let { tvHospitalName.text = it.name }

        dev {
            btnLogin.onLongClick {
                etUsername.setText("puy")
                etPwd.setText("hipuy")
                true
            }
        }
    }
}
