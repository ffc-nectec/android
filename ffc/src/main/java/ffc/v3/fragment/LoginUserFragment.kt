package ffc.v3.fragment


import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ffc.entity.Organization
import ffc.v3.BuildConfig

import ffc.v3.R
import ffc.v3.baseActivity
import ffc.v3.util.assertThat
import ffc.v3.util.inputAssertion
import ffc.v3.util.notNullOrBlank
import org.jetbrains.anko.support.v4.toast

class LoginUserFragment : Fragment(), View.OnClickListener {

    lateinit var inputLayoutUsername: TextInputLayout
    lateinit var inputLayoutPassword: TextInputLayout
    lateinit var tvHospitalName: TextView
    lateinit var etUsername: TextInputEditText
    lateinit var etPwd: TextInputEditText
    lateinit var btnLogin: Button
    lateinit var btnBack: Button

    lateinit var organization: Organization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login_user, container, false)
        initInstances(rootView, savedInstanceState)

        return rootView
    }

    private fun init(savedInstanceState: Bundle?) {

    }

    private fun initInstances(rootView: View?, savedInstanceState: Bundle?) {
        inputLayoutUsername = rootView!!.findViewById(R.id.inputLayoutUsername)
        inputLayoutPassword = rootView.findViewById(R.id.inputLayoutPassword)
        etUsername = rootView.findViewById(R.id.etUsername)
        etPwd = rootView.findViewById(R.id.etPwd)
        btnLogin = rootView.findViewById(R.id.btnLogin)
        btnBack = rootView.findViewById(R.id.btnBack)
        btnLogin.setOnClickListener(this)
        btnBack.setOnClickListener(this)

        // Debug User Login
        if (BuildConfig.DEBUG) {
            btnLogin.setOnLongClickListener {
                etUsername.setText("admin0")
                etPwd.setText("1234admin0")
                true
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnLogin -> {
                // Assert username and password
                val checkUsername = inputAssertion(inputLayoutUsername, etUsername,
                    getString(R.string.no_username))
                val checkPwd = inputAssertion(inputLayoutPassword, etPwd,
                    getString(R.string.no_password))

                if (checkUsername && checkPwd) {
                    // TODO: Login
                }
            }

            R.id.btnBack ->
                fragmentManager!!.popBackStack()
        }
    }

//    try {
//        assertThat(baseActivity.isOnline) { getString(R.string.please_check_connectivity) }
//        assertThat(usernameView.notNullOrBlank()) { getString(R.string.no_username) }
//        assertThat(passwordView.notNullOrBlank()) { getString(R.string.no_password) }
//
//        onLoginRequest(usernameView.text.toString(), passwordView.text.toString())
//    } catch (assert: IllegalArgumentException) {
//        toast(assert.message ?: "Error")
//    }

}
