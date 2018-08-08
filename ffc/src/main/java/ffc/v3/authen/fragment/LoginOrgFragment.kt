package ffc.v3.authen.fragment


import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import ffc.entity.Organization
import ffc.entity.gson.toJson
import ffc.v3.BuildConfig

import ffc.v3.R
import ffc.v3.authen.LoginInteractor
import ffc.v3.baseActivity
import ffc.v3.util.EventListener
import ffc.v3.util.assertionNotEmpty
import org.jetbrains.anko.support.v4.longToast


class LoginOrgFragment : Fragment(), View.OnClickListener {

    lateinit var etOrganization: EditText
    lateinit var btnNext: View

    lateinit var inputLayoutOrganization: TextInputLayout
    private lateinit var organizationName: String
    private lateinit var organization: Organization
    private lateinit var interactor: LoginInteractor
    private lateinit var eventListener: EventListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login_org, container, false)
        initInstances(rootView, savedInstanceState)

        return rootView
    }

    private fun initInstances(rootView: View, savedInstanceState: Bundle?) {
        inputLayoutOrganization = rootView.findViewById(R.id.inputLayoutOrganization)
        etOrganization = rootView.findViewById(R.id.etOrganization)

        btnNext = rootView.findViewById(R.id.btnNext)
        btnNext.setOnClickListener(this)

        interactor = LoginInteractor()
        organization = Organization()

        eventListener = activity as EventListener

        // Assert the Internet connection
        if (baseActivity.isOnline) {
            btnNext.visibility = View.VISIBLE
        }

        // Debug User Login
        if (BuildConfig.DEBUG) {
            btnNext.setOnLongClickListener {
                // onNext.invoke(organization)
                etOrganization.setText("รพ.สต. เนคเทค")
                true
            }
        }
    }

    private fun goLoginUserFragment(orgObject: Organization) {
        val fragment = LoginUserFragment()
        val fragmentManager = activity!!.supportFragmentManager

        // Pass Organization object to LoginUserFragment for login
        val bundle = Bundle()
        bundle.putString("organization", orgObject.toJson())

        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun checkOrganizationName(userNetwork: String) {
        interactor.requestMyOrg { orgList, t ->
            orgList.find { it.name == userNetwork }?.let {
                // User connects to the network's hospital or
                // enters the organization name correctly
                eventListener.onShowProgressBar(false)
                goLoginUserFragment(it)
            }
            t?.let {
                longToast(it.message ?: "message")
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnNext -> {
                organizationName = etOrganization.text.toString()

                // Check whether the organization name is entered
                val isOrgNameEmpty = assertionNotEmpty(inputLayoutOrganization,
                    organizationName,
                    getString(R.string.please_type_org))

                if (isOrgNameEmpty) {
                    // Check whether the user connects to the hospital's network
                    eventListener.onShowProgressBar(true)
                    checkOrganizationName(organizationName)
                }
            }
        }
    }
}
