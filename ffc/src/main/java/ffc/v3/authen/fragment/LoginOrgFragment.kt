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
import ffc.v3.util.inputAssertion
import org.jetbrains.anko.support.v4.longToast


class LoginOrgFragment : Fragment(), View.OnClickListener {

    lateinit var etOrganization: EditText
    lateinit var btnNext: View

    private val MY_ORG = 0
    private val ALL_ORG = 1

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
        // networkName is an Organization name
        val fragment = LoginUserFragment()
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        val bundle = Bundle()
        bundle.putString("organization", orgObject.toJson())

        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.contentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun checkNetworkName(userNetwork: String) {

        interactor.requestMyOrg {
            // User connects to the network's hospital
            if (it[0] == "Success") {
                checkOrganization(it, userNetwork, MY_ORG)

                // User doesn't connect to the hospital's network
            } else if (it[0] == 404) {
                interactor.requestAllOrg {
                    checkOrganization(it, userNetwork, ALL_ORG)
                }
            }
        }
    }

    private fun checkOrganization(it: MutableList<Any>, userNetwork: String, type: Int) {
        eventListener.onShowProgressBar(false)
        val org = findOrgWithId(it[1] as List<Organization>, userNetwork)

        if (org != null)
            goLoginUserFragment(org)
        else if (type == ALL_ORG)
            longToast(R.string.not_found_org)
    }

    private fun findOrgWithId(organizationList: List<Organization>, networkName: String): Organization? {
        for (i in 0 until organizationList.size)
            if (organizationList[i].name.equals(networkName, true)) {
                return organizationList[i]
            }

        return null
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnNext -> {
                // Assert an organization name
                organizationName = etOrganization.text.toString()
                val checkOrgName = inputAssertion(inputLayoutOrganization, organizationName,
                    getString(R.string.please_type_org))

                if (checkOrgName) {
                    // Whether the user connects to the hospital's network
                    eventListener.onShowProgressBar(true)
                    checkNetworkName(organizationName)
                }
            }
        }
    }
}
