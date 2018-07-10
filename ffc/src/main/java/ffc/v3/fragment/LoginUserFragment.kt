package ffc.v3.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import ffc.v3.R

class LoginUserFragment : Fragment(), View.OnClickListener {

    lateinit var tvHospitalName: TextView
    lateinit var etUsername: EditText
    lateinit var etPwd: EditText
    lateinit var btnLogin: Button
    lateinit var btnBack: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login_user, container, false)
        initInstances(rootView, savedInstanceState)

        return rootView
    }

    private fun initInstances(rootView: View?, savedInstanceState: Bundle?) {
        btnLogin = rootView!!.findViewById(R.id.btnLogin)
        btnBack = rootView.findViewById(R.id.btnBack)
        btnLogin.setOnClickListener(this)
        btnBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnLogin ->
                // TODO: Check Username and Password
                return

            R.id.btnBack ->
                fragmentManager!!.popBackStack()
        }
    }

}
