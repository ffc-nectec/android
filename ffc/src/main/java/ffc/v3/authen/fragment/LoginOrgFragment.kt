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
import ffc.v3.BuildConfig
import ffc.v3.R
import ffc.v3.authen.orgs
import ffc.v3.baseActivity
import ffc.v3.util.assertionNotEmpty
import ffc.v3.util.warnTextInput
import kotlinx.android.synthetic.main.fragment_login_org.btnNext
import kotlinx.android.synthetic.main.fragment_login_org.etOrganization
import kotlinx.android.synthetic.main.fragment_login_org.inputLayoutOrganization
import org.jetbrains.anko.support.v4.longToast

internal class LoginOrgFragment : Fragment(), View.OnClickListener {

    private lateinit var loginActivityListener: LoginActivityListener
    internal lateinit var orgSelected: (Organization) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_org, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loginActivityListener = activity as LoginActivityListener
        btnNext.setOnClickListener(this)

        if (baseActivity.isOnline) {
            btnNext.visibility = View.VISIBLE
        }

        checkUserNetwork()

        if (BuildConfig.DEBUG) {
            btnNext.setOnLongClickListener {
                etOrganization.setText("รพ.สต. เนคเทค")
                true
            }
        }
    }

    private fun checkUserNetwork() {
        orgs().myOrg {
            onFound {
                loginActivityListener.onShowProgressBar(false)
                orgSelected(it)
            }
        }
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnNext -> {
                val organizationName = etOrganization.text.toString()
                val isNotEmpty = assertionNotEmpty(
                    inputLayoutOrganization,
                    organizationName,
                    getString(R.string.please_type_org)
                )

                if (isNotEmpty) {
                    // Check whether the user connects to the hospital's network
                    loginActivityListener.onShowProgressBar(true)
                    findOrgBy(organizationName)
                }
            }
        }
    }

    private fun findOrgBy(orgQuery: String) {
        orgs().org(query = orgQuery) {
            onFound {
                loginActivityListener.onShowProgressBar(false)
                orgSelected(it)
            }
            onNotFound {
                warnTextInput(inputLayoutOrganization, getString(R.string.not_found_org), true)
            }
            onFail { longToast(it.message ?: "message") }
        }
    }
}
