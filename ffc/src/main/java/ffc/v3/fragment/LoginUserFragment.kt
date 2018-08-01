package ffc.v3.fragment


import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ffc.entity.Organization
import ffc.entity.gson.parseTo
import ffc.v3.BuildConfig
import ffc.v3.MapsActivity

import ffc.v3.R
import ffc.v3.authen.IdentityRepo
import ffc.v3.util.inputAssertion
import ffc.v3.authen.LoginInteractor
import ffc.v3.authen.LoginPresenter
import ffc.v3.authen.getIdentityRepo
import kotlinx.android.synthetic.main.fragment_login_user.*
import org.jetbrains.anko.support.v4.intentFor

class LoginUserFragment : Fragment(), View.OnClickListener, LoginPresenter {

    lateinit var orgIdBundle: Bundle
    lateinit var orgId: String
    lateinit var org: Organization

    lateinit var inputLayoutUsername: TextInputLayout
    lateinit var inputLayoutPassword: TextInputLayout
    lateinit var tvHospitalName: TextView
    lateinit var etUsername: TextInputEditText
    lateinit var etPwd: TextInputEditText
    lateinit var btnLogin: Button
    lateinit var btnBack: Button

    lateinit var organization: Organization
    private lateinit var interactor: LoginInteractor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login_user, container, false)
        initInstances(rootView, savedInstanceState)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        onLoginRequest = (baseActivity as LoginActivity).onLoginRequest
//        btnLogin.setOnClickListener(this)
//        btnBack.setOnClickListener(this)
    }

    private fun initInstances(rootView: View?, savedInstanceState: Bundle?) {
        orgIdBundle = arguments!!
        org = orgIdBundle.getString("organization").parseTo<Organization>()

        inputLayoutUsername = rootView!!.findViewById(R.id.inputLayoutUsername)
        inputLayoutPassword = rootView.findViewById(R.id.inputLayoutPassword)
        etUsername = rootView.findViewById(R.id.etUsername)
        etPwd = rootView.findViewById(R.id.etPwd)
        btnLogin = rootView.findViewById(R.id.btnLogin)
        btnBack = rootView.findViewById(R.id.btnBack)
        btnLogin.setOnClickListener(this)
        btnBack.setOnClickListener(this)

        interactor = LoginInteractor()
        interactor.loginPresenter = this
        interactor.idRepo = getIdentityRepo(context!!)

        // Debug User Login
        if (BuildConfig.DEBUG) {
            btnLogin.setOnLongClickListener {
                etUsername.setText("ploy")
                etPwd.setText("n")
                true
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnLogin -> {
                // Assert username and password
                val username = etUsername.text.toString()
                val password = etPwd.text.toString()
                val checkUsername = inputAssertion(inputLayoutUsername, username,
                    getString(R.string.no_username))
                val checkPwd = inputAssertion(inputLayoutPassword, password,
                    getString(R.string.no_password))

                if (checkUsername && checkPwd) {
                    // Login
                    interactor.org = org
                    interactor.doLogin(username, password)
                }
//                try {
//                    assertThat(baseActivity.isOnline) { getString(R.string.please_check_connectivity) }
//                    assertThat(etUsername.notNullOrBlank()) { getString(R.string.no_username) }
//                    assertThat(etPwd.notNullOrBlank()) { getString(R.string.no_password) }
//
//                    onLoginRequest.invoke(etUsername.text.toString(), etPwd.text.toString())
//
////                    interactor.doLogin(etUsername.text.toString(), etPwd.text.toString())
//
//                } catch (assert: IllegalArgumentException) {
//                    toast(assert.message ?: "Error")
//                }
            }

            R.id.btnBack ->
                fragmentManager!!.popBackStack()
        }
    }

    override fun onLoginSuccess() {
        Log.d("test", "onLoginSuccess()")
        startActivity(intentFor<MapsActivity>())
    }

    override fun onError(message: Int) {
        Log.d("test", context!!.getString(message))
    }

    override fun onError(message: String) {
        Log.d("test", message)
    }
}
