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

package ffc.v3.authen

import ffc.entity.Organization
import ffc.v3.api.FfcCentral
import ffc.v3.api.OrgService
import ffc.v3.authen.exception.LoginErrorException
import ffc.v3.authen.exception.LoginFailureException
import okhttp3.Credentials
import retrofit2.dsl.enqueue
import java.nio.charset.Charset

internal class LoginInteractor() {

    lateinit var loginPresenter: LoginPresenter
    lateinit var idRepo: IdentityRepo
    private val orgService = FfcCentral().service<OrgService>()

    var org: Organization? = null
        set(value) {
            field = value
            if (value != null) loginPresenter.onOrgSelected(value)
        }

    private val utf8 = Charset.forName("UTF-8")

    fun doLogin(username: String, password: String) {
        check(org != null) { "Must set org before" }
        val basicToken = Credentials.basic(username.trim(), password.trim(), utf8)
        orgService.createAuthorize(org!!.id, basicToken).enqueue {
            onSuccess {
                val authorize = body()!!
                FfcCentral.TOKEN = authorize.token
                idRepo.org = org
                idRepo.token = authorize.token
                users().user(username) { user, t ->
                    if (user != null) {
                        idRepo.user = user
                        loginPresenter.onLoginSuccess()
                    } else {
                        loginPresenter.onError(t ?: IllegalStateException("Something wrong"))
                    }
                }

            }
            onError {
                loginPresenter.onError(LoginErrorException())
            }
            onFailure {
                loginPresenter.onError(LoginFailureException(it.message ?: "Something wrong"))
            }
        }
    }

    val isLoggedIn: Boolean
        get() {
            return false
            if (idRepo.user != null) {
                FfcCentral.TOKEN = idRepo.token
                return true
            } else
                return false
        }

}
