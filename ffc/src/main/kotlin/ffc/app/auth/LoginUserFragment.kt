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

package ffc.app.auth

//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ffc.android.check
import ffc.android.hideSoftKeyboard
import ffc.android.invisible
import ffc.android.isNotBlank
import ffc.android.observe
import ffc.android.onClick
import ffc.android.onLongClick
import ffc.android.viewModel
import ffc.android.visible
import ffc.app.R
import ffc.app.dev
import ffc.entity.Organization
import ffc.entity.gson.parseTo
import ffc.entity.gson.toJson
import kotlinx.android.synthetic.main.hs_service_item.view.*
import kotlinx.android.synthetic.main.login_user_fragment.btnBack
import kotlinx.android.synthetic.main.login_user_fragment.btnLogin
import kotlinx.android.synthetic.main.login_user_fragment.emptyView
import kotlinx.android.synthetic.main.login_user_fragment.errorView
import kotlinx.android.synthetic.main.login_user_fragment.etPwd
import kotlinx.android.synthetic.main.login_user_fragment.etUsername
import kotlinx.android.synthetic.main.login_user_fragment.tvHospitalName
import org.jetbrains.anko.bundleOf

internal class LoginUserFragment : Fragment(), LoginExceptionPresenter {

    private val viewModel by lazy { viewModel<LoginViewModel>() }

    lateinit var onLogin: (username: String, password: String) -> Unit
    var org: Organization?
        set(value) {
            arguments = bundleOf("organization" to value?.toJson())
        }
        get() = arguments?.getString("organization")?.parseTo()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observe(viewModel.loading) {
            if (it == true) emptyView.showLoading() else emptyView.showContent()
        }
        observe(viewModel.exception) {
            if (it == null) {
                errorView.invisible()
            } else {
                emptyView.showContent()
                errorView.visible()
                errorView.text = it.message ?: "ERROR"
            }
        }

        etPwd.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnLogin.performClick()
                v.hideSoftKeyboard()
                return@setOnEditorActionListener true
            } else return@setOnEditorActionListener false
        }

        btnLogin.onClick {
            try {
                viewModel.exception.value = null
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

                viewModel.loading.value = true
                onLogin(username, password)
            } catch (handled: IllegalStateException) {
            }
        }
        btnBack.onClick { activity?.onBackPressed() }

        org!!.let { tvHospitalName.text = it.displayName }

        dev {
            btnLogin.onLongClick {
                etUsername.setText("puy")
                etPwd.setText("hipuy")
                true
            }
        }
    }

    override fun onException(throwable: Throwable) {
        viewModel.exception.value = throwable
    }

    class LoginViewModel : ViewModel() {
        val loading = MutableLiveData<Boolean>()
        val exception = MutableLiveData<Throwable>()
    }
}
