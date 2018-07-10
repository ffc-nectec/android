package ffc.v3.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import ffc.v3.R
import kotlinx.android.synthetic.main.fragment_login_org.*

class LoginOrgFragment : Fragment(), View.OnClickListener {

    lateinit var etOrganization: EditText
    lateinit var btnNext: View
    lateinit var organization: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login_org, container, false)
        initInstances(rootView, savedInstanceState)

        return rootView
    }

    private fun initInstances(rootView: View, savedInstanceState: Bundle?) {
        etOrganization = rootView.findViewById(R.id.etOrganization)
        organization = etOrganization.text.toString()

        btnNext = rootView.findViewById(R.id.btnNext)
        btnNext.setOnClickListener(this)
    }

    @SuppressLint("CommitTransaction")
    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.btnNext -> {
                // TODO: Check Organization Unit
                
                val fragment = LoginUserFragment()
                val fragmentManager = activity!!.supportFragmentManager
                val fragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction.replace(R.id.contentContainer, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }
}
