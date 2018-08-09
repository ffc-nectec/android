package ffc.v3.authen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.entity.Organization
import ffc.v3.BuildConfig
import ffc.v3.R
import ffc.v3.baseActivity
import ffc.v3.util.assertThat
import ffc.v3.util.notNullOrBlank
import kotlinx.android.synthetic.main.auth_fragment_user_pass.passwordView
import kotlinx.android.synthetic.main.auth_fragment_user_pass.submitView
import kotlinx.android.synthetic.main.auth_fragment_user_pass.usernameView
import org.jetbrains.anko.support.v4.toast

class UserPassFragment : Fragment() {

    lateinit var organization: Organization
    lateinit var onLoginRequest: (username: String, password: String) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.auth_fragment_user_pass, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (BuildConfig.DEBUG) {
            submitView.setOnLongClickListener {
                usernameView.setText("ploy")
                passwordView.setText("n")
                true
            }
        }

        submitView.setOnClickListener {
            try {
                assertThat(baseActivity.isOnline) { getString(R.string.please_check_connectivity) }
                assertThat(usernameView.notNullOrBlank()) { getString(R.string.no_username) }
                assertThat(passwordView.notNullOrBlank()) { getString(R.string.no_password) }

                onLoginRequest(usernameView.text.toString(), passwordView.text.toString())
            } catch (assert: IllegalArgumentException) {
                toast(assert.message ?: "Error")
            }
        }
    }
}
