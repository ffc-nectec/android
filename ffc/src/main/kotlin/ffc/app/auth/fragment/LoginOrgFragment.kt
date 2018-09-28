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

package ffc.app.auth.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.check
import ffc.android.error
import ffc.android.isNotBlank
import ffc.android.onClick
import ffc.app.R
import ffc.app.auth.orgs
import ffc.app.dev
import ffc.app.familyFolderActivity
import ffc.entity.Organization
import kotlinx.android.synthetic.main.fragment_login_org.btnNext
import kotlinx.android.synthetic.main.fragment_login_org.etOrganization
import org.jetbrains.anko.support.v4.longToast

internal class LoginOrgFragment : Fragment() {

    private val loginActivityListener: LoginActivityListener by lazy { activity as LoginActivityListener }

    lateinit var orgSelected: (Organization) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_org, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnNext.onClick {
            try {
                etOrganization.check {
                    that { isNotBlank }
                    message = getString(R.string.please_type_org)
                }

                findOrgBy(etOrganization.text.toString())
            } catch (handled: IllegalStateException) {
            }
        }

        if (familyFolderActivity.isOnline) {
            btnNext.visibility = View.VISIBLE
        }

        checkUserNetwork()

        dev {
            btnNext.setOnLongClickListener {
                etOrganization.setText("รพ.สต. เนคเทค")
                true
            }
        }
    }

    private fun checkUserNetwork() {
        orgs().myOrg {
            always { loginActivityListener.onShowProgressBar(false) }
            onFound {
                etOrganization.setText(it.name)
                orgSelected(it)
            }
        }
    }

    private fun findOrgBy(orgQuery: String) {
        loginActivityListener.onShowProgressBar(true)
        orgs().org(query = orgQuery.trim()) {
            always { loginActivityListener.onShowProgressBar(false) }
            onFound { orgSelected(it) }
            onNotFound { etOrganization.error(getString(R.string.not_found_org)) }
            onFail { longToast(it.message ?: "message") }
        }
    }
}
