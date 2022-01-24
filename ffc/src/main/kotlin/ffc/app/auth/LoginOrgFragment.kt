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

package ffc.app.auth

//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ffc.android.check
import ffc.android.error
import ffc.android.hideSoftKeyboard
import ffc.android.invisible
import ffc.android.isNotBlank
import ffc.android.observe
import ffc.android.onClick
import ffc.android.viewModel
import ffc.android.visible
import ffc.app.R
import ffc.app.dev
import ffc.app.familyFolderActivity
import ffc.entity.Organization
import kotlinx.android.synthetic.main.login_org_fragment.btnNext
import kotlinx.android.synthetic.main.login_org_fragment.emptyView
import kotlinx.android.synthetic.main.login_org_fragment.errorView
import kotlinx.android.synthetic.main.login_org_fragment.etOrganization
import me.piruin.spinney.Spinney
import me.piruin.spinney.SpinneyAdapter
import me.piruin.spinney.SpinneyDialog

internal class LoginOrgFragment : Fragment(), LoginExceptionPresenter {

    lateinit var orgSelected: (Organization) -> Unit

    lateinit var viewModel: OrgViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_org_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModel()
        observe(viewModel.orgs) { orgs ->
            when (orgs?.size) {
                0 -> etOrganization.error(getString(R.string.not_found_org))
                1 -> viewModel.selectOrg.value = orgs[0]
                else -> orgs?.let { showOrgSelectorUi(orgs) }
            }
            viewModel.loading.value = false
        }
        observe(viewModel.selectOrg) {
            if (it != null) {
                orgSelected(it)
            }
        }
        observe(viewModel.exception) {
            if (it == null) {
                errorView.invisible()
            } else {
                emptyView.showContent()
                errorView.visible()
                errorView.text = it.message ?: "ERROR"
            }
        }
        observe(viewModel.loading) {
            if (it == true)
                emptyView.showLoading()
            else
                emptyView.showContent()
        }

        etOrganization.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnNext.performClick()
                v.hideSoftKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

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
        viewModel.loading.value = true
        orgs().myOrg {
            onFound { viewModel.orgs.value = it }
            onNotFound { viewModel.orgs.value = null }
            onFail { viewModel.exception.value = it }
        }
    }

    private fun findOrgBy(orgQuery: String) {
        viewModel.loading.value = true
        viewModel.exception.value = null
        orgs().org(query = orgQuery.trim()) {
            onFound { viewModel.orgs.value = it }
            onNotFound { viewModel.orgs.value = listOf() }
            onFail { viewModel.exception.value = it }
        }
    }

    override fun onException(throwable: Throwable) {
        viewModel.exception.value = throwable
    }

    class OrgViewModel : ViewModel() {
        val orgs: MutableLiveData<List<Organization>> = MutableLiveData()
        val loading: MutableLiveData<Boolean> = MutableLiveData()
        val selectOrg: MutableLiveData<Organization> = MutableLiveData()
        val exception: MutableLiveData<Throwable> = MutableLiveData()
    }
}
