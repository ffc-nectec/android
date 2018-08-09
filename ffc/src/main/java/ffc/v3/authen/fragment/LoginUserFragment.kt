package ffc.v3.authen.fragment

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ffc.entity.Organization
import ffc.entity.gson.parseTo
import ffc.v3.BuildConfig
import ffc.v3.R
import ffc.v3.authen.LoginErrorException
import ffc.v3.authen.LoginFailureException
import ffc.v3.authen.LoginInteractor
import ffc.v3.authen.LoginPresenter
import ffc.v3.authen.getIdentityRepo
import ffc.v3.util.LoginEventListener
import ffc.v3.util.assertionNotEmpty
import org.jetbrains.anko.support.v4.longToast

class LoginUserFragment : Fragment(), View.OnClickListener, LoginPresenter {

    private lateinit var orgIdBundle: Bundle
    lateinit var org: Organization

    lateinit var inputLayoutUsername: TextInputLayout
    lateinit var inputLayoutPassword: TextInputLayout
    lateinit var etUsername: TextInputEditText
    lateinit var etPwd: TextInputEditText
    lateinit var btnLogin: Button
    lateinit var btnBack: Button

    lateinit var organization: Organization
    private lateinit var interactor: LoginInteractor
    private lateinit var loginEventListener: LoginEventListener
    private lateinit var loginPresenter: LoginPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login_user, container, false)
        initInstances(rootView, savedInstanceState)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnLogin.setOnClickListener(this)
        btnBack.setOnClickListener(this)
    }

    private fun initInstances(rootView: View?, savedInstanceState: Bundle?) {
        orgIdBundle = arguments!!
        org = orgIdBundle.getString("organization").parseTo()

        val tvHospitalName = rootView!!.findViewById<TextView>(R.id.tvHospitalName)
        inputLayoutUsername = rootView.findViewById(R.id.inputLayoutUsername)
        inputLayoutPassword = rootView.findViewById(R.id.inputLayoutPassword)
        etUsername = rootView.findViewById(R.id.etUsername)
        etPwd = rootView.findViewById(R.id.etPwd)
        btnLogin = rootView.findViewById(R.id.btnLogin)
        btnBack = rootView.findViewById(R.id.btnBack)
        btnLogin.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        loginEventListener = activity as LoginEventListener

        tvHospitalName.text = org.name

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
                val checkUsername = assertionNotEmpty(inputLayoutUsername, username,
                    getString(R.string.no_username))
                val checkPwd = assertionNotEmpty(inputLayoutPassword, password,
                    getString(R.string.no_password))

                // Login
                if (checkUsername && checkPwd) {
                    loginEventListener.onShowProgressBar(true)
                    interactor.org = org
                    interactor.doLogin(username, password)
                }
            }

            R.id.btnBack ->
                fragmentManager!!.popBackStack()
        }
    }

    override fun onLoginSuccess() {
        // Start MapActivity
        loginEventListener.onShowProgressBar(false)
        loginEventListener.onLoginActivity()
    }

    override fun onError(throwable: Throwable) {
        loginEventListener.onShowProgressBar(false)
        val message = when (throwable) {
            is LoginErrorException -> getString(R.string.identification_error)
            is LoginFailureException -> throwable.message
            else -> "Something wrong"
        }
        message?.let { longToast(it) }
    }
}
