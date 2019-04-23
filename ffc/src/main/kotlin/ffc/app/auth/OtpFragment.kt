package ffc.app.auth

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import ffc.android.addTextWatcher
import ffc.android.hideSoftKeyboard
import ffc.android.invisible
import ffc.android.observe
import ffc.android.onClick
import ffc.android.viewModel
import ffc.android.visible
import ffc.app.R
import kotlinx.android.synthetic.main.login_otp_fragment.authenBtn
import kotlinx.android.synthetic.main.login_otp_fragment.emptyView
import kotlinx.android.synthetic.main.login_otp_fragment.errorView
import kotlinx.android.synthetic.main.login_otp_fragment.otpInput

class OtpFragment : Fragment(), LoginExceptionPresenter {

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
