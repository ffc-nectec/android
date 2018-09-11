/*
 * Copyright (c) 2018 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import ffc.v3.util.warnTextInput
import kotlinx.android.synthetic.main.fragment_login_org.btnNext
import kotlinx.android.synthetic.main.fragment_login_org.etOrganization
import kotlinx.android.synthetic.main.fragment_login_org.inputLayoutOrganization
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

        // Whether the user login with organization's network
        checkUserNetwork()

        // Debug User Login
        if (BuildConfig.DEBUG) {
            btnNext.setOnLongClickListener {
                // onNext.invoke(organization)
                etOrganization.setText("รพ.สต. เนคเทค")
                true
            }
        }
    }

    private fun goLoginUserFragment(orgObject: Organization, orgNetwork: Boolean) {
        val fragment = LoginUserFragment()
        val fragmentManager = activity!!.supportFragmentManager

        // Pass Organization object to LoginUserFragme
        val bundle = Bundle()
        bundle.putString("organization", orgObject.toJson())

        fragment.arguments = bundle
        if (orgNetwork)
            fragmentManager.beginTransaction()
                .replace(R.id.contentContainer, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun checkUserNetwork() {
        interactor.requestMyOrg { orgList, t ->
            loginEventListener.onShowProgressBar(false)
            if (!orgList.isEmpty())
                goLoginUserFragment(orgList[0], true)
        }
    }

    private fun checkOrganizationName(userNetwork: String) {
        interactor.requestAllOrg { orgList, t ->
            loginEventListener.onShowProgressBar(false)

            orgList.find { it.name == userNetwork }?.let {
                // User enters the organization name correctly
                goLoginUserFragment(it, false)
            } ?: run {
                warnTextInput(
                    inputLayoutOrganization,
                    getString(R.string.not_found_org),
                    true
                )
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
                val isOrgNameEmpty = assertionNotEmpty(
                    inputLayoutOrganization,
                    organizationName,
                    getString(R.string.please_type_org)
                )

                if (isOrgNameEmpty) {
                    // Check whether the user connects to the hospital's network
                    loginEventListener.onShowProgressBar(true)
                    checkOrganizationName(organizationName)
                }
            }
        }
    }
}
