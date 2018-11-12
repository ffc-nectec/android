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

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.check
import ffc.android.error
import ffc.android.isNotBlank
import ffc.android.observe
import ffc.android.onClick
import ffc.android.viewModel
import ffc.app.R
import ffc.app.auth.orgs
import ffc.app.dev
import ffc.app.familyFolderActivity
import ffc.entity.Organization
import kotlinx.android.synthetic.main.fragment_login_org.btnNext
import kotlinx.android.synthetic.main.fragment_login_org.etOrganization
import me.piruin.spinney.Spinney
import me.piruin.spinney.SpinneyAdapter
import me.piruin.spinney.SpinneyDialog
import org.jetbrains.anko.support.v4.longToast

internal class LoginOrgFragment : Fragment() {

    private val loginActivityListener: LoginActivityListener by lazy { activity as LoginActivityListener }

    lateinit var orgSelected: (Organization) -> Unit

    lateinit var viewModel: OrgViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_org, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModel()
        observe(viewModel.orgs) {
            loginActivityListener.onShowProgressBar(false)
            when (it?.size) {
                null -> {
                }
                0 -> etOrganization.error(getString(R.string.not_found_org))
                1 -> viewModel.selectOrg.value = it[0]
                else -> showOrgSelectorUi(it)
            }
        }
        observe(viewModel.selectOrg) {
            if (it != null) {
                etOrganization.setText(it.name)
                orgSelected(it)
            }
        }
        observe(viewModel.exception) { longToast(it?.message ?: "Something Error Occurs") }

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

    private fun showOrgSelectorUi(orgs: List<Organization>) {

        with(SpinneyDialog(context)) {
            val presenter = Spinney.ItemPresenter<Organization> { item, _ -> item.displayName }
            setHint("เลือกหน่วยงาน")
            setAdapter(SpinneyAdapter(context, orgs, presenter))
            setOnItemSelectedListener { item, _ ->
                viewModel.selectOrg.value = item as Organization
                true
            }
            show()
        }
    }

    private fun checkUserNetwork() {
        orgs().myOrg {
            onFound { viewModel.orgs.value = it }
            onNotFound { viewModel.orgs.value = null }
            onFail { viewModel.exception.value = it }
        }
    }

    private fun findOrgBy(orgQuery: String) {
        loginActivityListener.onShowProgressBar(true)
        orgs().org(query = orgQuery.trim()) {
            onFound { viewModel.orgs.value = it }
            onNotFound { viewModel.orgs.value = listOf() }
            onFail { viewModel.exception.value = it }
        }
    }

    class OrgViewModel : ViewModel() {

        val orgs: MutableLiveData<List<Organization>> = MutableLiveData()
        val selectOrg: MutableLiveData<Organization> = MutableLiveData()
        val exception: MutableLiveData<Throwable> = MutableLiveData()
    }
}
