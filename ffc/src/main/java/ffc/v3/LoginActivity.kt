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
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.password_layout
import kotlinx.android.synthetic.main.activity_login.submit
import kotlinx.android.synthetic.main.activity_login.username
import kotlinx.android.synthetic.main.activity_login.username_layout
import me.piruin.spinney.Spinney
import okhttp3.Credentials
import java.nio.charset.Charset

class LoginActivity : AppCompatActivity() {

  val organization by lazy { findViewById<Spinney<Org>>(R.id.org) }

  private val orgService = FfcCentral().call<OrgService>()

  val model: LoginViewModel by lazy { viewModel<LoginViewModel>() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    organization.gone()
    organization.setItemPresenter { item, position -> (item as Org).name }
    organization.setOnItemSelectedListener { _, selectedItem, _ ->
      model.choosedOrg.value = selectedItem
    }

    if (model.orgList.value?.isEmpty() == true)
      requestMyOrg()
    else {
      organization.setItems(model.orgList.value!!)
      organization.visible()
    }

    model.orgList.observe(this) {
      organization.setItems(it!!)
      organization.visible()

      if (it.size == 1) organization.selectedItem = it[0]
    }

    if (model.choosedOrg.value != null) {
      organization.selectedItem = model.choosedOrg.value
    } else {
      username_layout.gone()
      password_layout.gone()
      submit.gone()
    }

    model.choosedOrg.observe(this) {
      if (it != null) {
        username_layout.visible()
        password_layout.visible()
        submit.visible()
      } else {
        username_layout.gone()
        password_layout.gone()
        submit.gone()
      }
    }

    submit.setOnClickListener {
      try {
        doLogin(username.text.toString(), password.text.toString())
      } catch (assert: RuntimeException) {
        Toast.makeText(this, assert.message, Toast.LENGTH_SHORT).show()
      }
    }
  }

  private fun doLogin(username: String?, password: String?) {
    assertThat(!username.isNullOrBlank()) { "กรุณาระบุ username" }
    assertThat(!password.isNullOrBlank()) { "กรุณาระบุ password" }

    val basicToken =
      Credentials.basic(username!!.trim(), password!!.trim(), Charset.forName("UTF-8"))
    debug("Basic Auth = %s", basicToken)

    orgService.createAuthorize(model.choosedOrg.value!!.id, basicToken)
      .then { response, throwable ->
        response?.let {
          if (it.isSuccessful) {
            Toast.makeText(this, "Token ${it.body()!!.token}", Toast.LENGTH_SHORT).show()
          } else {
            Toast.makeText(this, "Not Success", Toast.LENGTH_SHORT).show()
          }
        }
      }
  }

  private fun requestMyOrg() {
    orgService.myOrg().then { res, t ->
      res?.let {
        if (it.isSuccessful && it.body() != null) {
          model.orgList.value = it.body()
        } else {
          requestOrgList()
        }
      }
      t?.let { }
    }
  }

  private fun requestOrgList() {
    orgService.listOrgs().then { res, t ->
      res?.let {
        if (it.isSuccessful && it.body() != null) {
          if (it.body()!!.isNotEmpty()) model.orgList.value = it.body()!!
        } else {
          Toast.makeText(this, "Not found Org List", Toast.LENGTH_SHORT).show()
        }
      }
      t?.let { }
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
