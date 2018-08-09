package ffc.v3.authen.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.entity.Organization
import ffc.entity.gson.toJson
import ffc.v3.BuildConfig
import ffc.v3.R
import ffc.v3.authen.LoginInteractor
import ffc.v3.authen.getIdentityRepo
import ffc.v3.baseActivity
import ffc.v3.util.LoginEventListener
import ffc.v3.util.assertionNotEmpty
import kotlinx.android.synthetic.main.fragment_login_org.*
import org.jetbrains.anko.support.v4.longToast

class LoginOrgFragment : Fragment(), View.OnClickListener {

    private lateinit var organizationName: String
    private lateinit var organization: Organization
    private lateinit var loginEventListener: LoginEventListener
    private lateinit var interactor: LoginInteractor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_org, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loginEventListener = activity as LoginEventListener
        btnNext.setOnClickListener(this)

        organization = Organization()
        interactor = LoginInteractor()
        interactor.idRepo = getIdentityRepo(context!!)

        if (interactor.idRepo.token != null)
            loginEventListener.onLoginActivity()

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

        // Pass Organization object to LoginUserFragme
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
                loginEventListener.onShowProgressBar(false)
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
                    loginEventListener.onShowProgressBar(true)
                    checkOrganizationName(organizationName)
                }
            }
        }
    }
}
