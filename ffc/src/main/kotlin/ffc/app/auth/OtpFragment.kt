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
import ffc.android.addTextWatcher
import ffc.android.hideSoftKeyboard
import ffc.android.invisible
import ffc.android.observe
import ffc.android.onClick
import ffc.android.viewModel
import ffc.android.visible
import ffc.app.R
import kotlinx.android.synthetic.main.hs_service_item.view.*
import kotlinx.android.synthetic.main.login_otp_fragment.authenBtn
import kotlinx.android.synthetic.main.login_otp_fragment.btnBack
import kotlinx.android.synthetic.main.login_otp_fragment.emptyView
import kotlinx.android.synthetic.main.login_otp_fragment.errorView
import kotlinx.android.synthetic.main.login_otp_fragment.otpHint
import kotlinx.android.synthetic.main.login_otp_fragment.otpInput
import org.jetbrains.anko.support.v4.browse

class OtpFragment : Fragment(), LoginExceptionPresenter {

    private val otpHintAnimate = "https://raw.githubusercontent.com/ffc-nectec/assets/master/manual/otp.gif"

    val viewModel by lazy { viewModel<OtpViewModel>() }

    lateinit var onOtpSend: (otp: String) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_otp_fragment, container, false)
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
        observe(viewModel.otp) { otp ->
            authenBtn.isEnabled = otp?.length == 6
        }

        authenBtn.onClick {
            viewModel.exception.value = null
            viewModel.loading.value = true
            onOtpSend(viewModel.otp.value!!)
        }

        otpInput.apply {
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    authenBtn.performClick()
                    v.hideSoftKeyboard()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            addTextWatcher {
                onTextChanged { text, _, _, _ -> viewModel.otp.value = text?.toString() }
            }
        }
        otpHint.onClick { browse(otpHintAnimate, true) }

        btnBack.onClick { activity?.onBackPressed() }
    }

    override fun onException(throwable: Throwable) {
        viewModel.exception.value = throwable
    }

    class OtpViewModel : ViewModel() {
        val otp: MutableLiveData<String> = MutableLiveData()
        val loading: MutableLiveData<Boolean> = MutableLiveData()
        val exception: MutableLiveData<Throwable> = MutableLiveData()
    }
}
