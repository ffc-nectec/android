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
import ffc.v3.authen.*
import ffc.v3.util.inputAssertion
import ffc.v3.util.EventListener
import org.jetbrains.anko.support.v4.longToast

class LoginUserFragment : Fragment(), View.OnClickListener, LoginPresenter {

    private lateinit var orgIdBundle: Bundle
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
    private lateinit var eventListener: EventListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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

        eventListener = activity as EventListener

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

                // Login
                if (checkUsername && checkPwd) {
                    eventListener.onShowProgressBar(true)
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
        eventListener.onShowProgressBar(false)
        //startActivity(intentFor<MapsActivity>())
    }

    override fun onError(throwable: Throwable) {
        eventListener.onShowProgressBar(false)
        val message = when (throwable) {
            is LoginErrorException -> getString(R.string.identification_error)
            is LoginFailureException -> throwable.message
            else -> "Something wrong"
        }
        message?.let { longToast(it) }
    }
}
