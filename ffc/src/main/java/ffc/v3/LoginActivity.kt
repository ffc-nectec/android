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

package ffc.v3

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.view.View
import ffc.v3.api.FfcCentral
import ffc.v3.api.OrgService
import ffc.v3.util.assertThat
import ffc.v3.util.debug
import ffc.v3.util.debugToast
import ffc.v3.util.get
import ffc.v3.util.gone
import ffc.v3.util.notNullOrBlank
import ffc.v3.util.observe
import ffc.v3.util.put
import ffc.v3.util.toJson
import ffc.v3.util.viewModel
import ffc.v3.util.visible
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.password_layout
import kotlinx.android.synthetic.main.activity_login.submit
import kotlinx.android.synthetic.main.activity_login.username
import kotlinx.android.synthetic.main.activity_login.username_layout
import me.piruin.spinney.Spinney
import okhttp3.Credentials
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.toast
import org.joda.time.LocalDate
import retrofit2.dsl.enqueue
import retrofit2.dsl.then
import java.nio.charset.Charset

class LoginActivity : BaseActivity() {

  private val organization by lazy { findViewById<Spinney<Org>>(R.id.org) }
  private val orgService = FfcCentral().service<OrgService>()
  private val viewModel: LoginViewModel by lazy { viewModel<LoginViewModel>() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    toast("is Online = $isOnline")

    val authorize = defaultSharedPreferences.get<Authorize>("token")
    if (authorize?.isValid == true) {
      debugToast("Use last token")
      FfcCentral.TOKEN = authorize.token
      startActivity(intentFor<MapsActivity>())
      finish()
      return
    }

    organization.gone()
    organization.setItemPresenter { item, _ -> (item as Org).name }
    organization.setOnItemSelectedListener { _, selectedItem, _ ->
      viewModel.choosedOrg.value = selectedItem
    }

    submit.setOnClickListener {
      try {
        assertThat(isOnline) { "กรุณาเชื่อมต่ออินเตอร์เน็ต" }
        doLogin(username.text.toString(), password.text.toString())
      } catch (assert: RuntimeException) {
        toast(assert.message ?: "Error")
      }
    }
    if (BuildConfig.DEBUG) {
      submit.setOnLongClickListener {
        username.setText("admin0")
        password.setText("1234admin0")
        true
      }
    }

    if (viewModel.orgList.value?.isEmpty() == true)
      requestMyOrg()
    else {
      organization.setItems(viewModel.orgList.value!!)
      organization.visible()
    }

    if (viewModel.choosedOrg.value != null) {
      organization.selectedItem = viewModel.choosedOrg.value
    } else {
      username_layout.gone()
      password_layout.gone()
      submit.gone()
    }

    viewModel.orgList.observe(this) {
      organization.setItems(it!!)
      organization.visible()

      if (it.size == 1) organization.selectedItem = it[0]
    }

    viewModel.choosedOrg.observe(this) {
      val visible = if (it != null) View.VISIBLE else View.GONE
      username_layout.visibility = visible
      password_layout.visibility = visible
      submit.visibility = visible
    }
  }

  private fun doLogin(username: String?, password: String?) {
    assertThat(username.notNullOrBlank()) { "กรุณาระบุ username" }
    assertThat(password.notNullOrBlank()) { "กรุณาระบุ password" }

    val basicToken =
      Credentials.basic(username!!.trim(), password!!.trim(), Charset.forName("UTF-8"))
    debug("Basic Auth = %s", basicToken)

    val dialog = progressDialog("Checking Authorize..")
    val org = viewModel.choosedOrg.value!!
    orgService.createAuthorize(org.id, basicToken).enqueue {
      always {
        dialog.dismiss()
      }
      onSuccess {
        val authorize = body()!!
        if (authorize.expireDate == null)
          authorize.expireDate = LocalDate.now().plusDays(1)
        debugToast("Authorize ${authorize.toJson()}")
        FfcCentral.TOKEN = authorize.token
        defaultSharedPreferences.edit()
          .put("token", authorize)
          .put("org", org)
          .apply()
        startActivity(intentFor<MapsActivity>())
      }
      onFailure {
        toast(it.message!!)
      }
    }
  }

  override fun onConnectivityChanged(isConnect: Boolean, message: String) {
    super.onConnectivityChanged(isConnect, "please check internet connection")
  }

  private fun requestMyOrg() {
    orgService.myOrg().then {
      viewModel.orgList.value = it
    }.catch { _, t ->
      requestOrgList()
      t?.let { toast(it.message ?: it.toString()) }
    }
  }

  private fun requestOrgList() {
    orgService.listOrgs().then {
      if (it.isEmpty()) {
        toast("Not found Org List")
        viewModel.orgList.value = listOf()
      } else {
        viewModel.orgList.value = it
      }
    }.catch { _, t ->
      t?.let { toast(it.message ?: it.toString()) }
    }
  }

  class LoginViewModel : ViewModel() {
    val orgList: MutableLiveData<List<Org>> by lazy {
      MutableLiveData<List<Org>>().apply {
        value = listOf()
      }
    }
    val choosedOrg: MutableLiveData<Org> by lazy { MutableLiveData<Org>() }
  }
}
