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

import ffc.api.FfcCentral
import ffc.app.auth.exception.LoginErrorException
import ffc.app.auth.exception.LoginFailureException
import ffc.entity.Organization
import ffc.entity.Token
import retrofit2.dsl.enqueue
import java.nio.charset.Charset

internal class LoginInteractor(
    var presenter: LoginPresenter,
    var auth: Authentication,
    relogin: Boolean = false
) {
    private val orgService = FfcCentral().service<OrgService>()

    init {
        if (relogin) {
            orgService.listOrgs(auth.org!!.name).enqueue {
                onSuccess {
                    org = body()!!.get(0)
                }
                onError {
                    presenter.showOrgSelector()
                }
                onFailure {
                    presenter.showOrgSelector()
                    presenter.onError(LoginFailureException(it.message ?: "Something wrong"))
                }
            }
        } else {
            if (auth.isLoggedIn) {
                FfcCentral.token = auth.token
                presenter.onLoginSuccess()
            } else {
                presenter.showOrgSelector()
            }
        }
    }

    var org: Organization? = null
        set(value) {
            field = value
            if (value != null) presenter.onOrgSelected(value)
        }

    private val utf8 = Charset.forName("UTF-8")

    private var credential: LoginBody? = null

    fun doLogin(username: String, password: String) {
        check(org != null) { "Must set org before" }
        val loginService = FfcCentral().service<AuthService>()
        loginService.createAuthorize(org!!.id, LoginBody(username.trim(), password.trim())).enqueue {
            onSuccess {
                onAuthorized(body()!!)
            }
            onError {
                val error = errorBody<AuthError>()
                if (!error?.redirect.isNullOrBlank()) {
                   credential = LoginBody(username.trim(), password.trim())
                   presenter.onActivateRequire()
                } else {
                    presenter.onError(LoginErrorException(this))
                }
            }
            onFailure {
                presenter.onError(LoginFailureException(it.message
                    ?: "เกิดข้อผิดพลาดไม่สามารถระบุได้"))
            }
        }
    }

    fun doActivate(otp: String) {
        check(org != null) { "Must set org before" }
        check(credential != null) { "Must login before" }
        val service = FfcCentral().service<AuthService>()
        service.activateUser(org!!.id, ActivateBody(otp, credential!!)).enqueue {
            onSuccess {
                onAuthorized(body()!!)
            }
            onError {
                presenter.onError(LoginErrorException(this))
            }
            onFailure {
                presenter.onError(LoginFailureException(it.message
                    ?: "เกิดข้อผิดพลาดไม่สามารถระบุได้"))
            }
        }
    }

    private fun onAuthorized(credential: Token) {
        FfcCentral.token = credential.token
        auth.org = org
        auth.token = credential.token
        auth.user = credential.user
        users().user(credential.user.name) { user, t ->
            if (user != null) {
                presenter.onLoginSuccess()
            } else {
                presenter.onError(LoginFailureException(t?.message
                    ?: "เกิดข้อผิดพลาดไม่สามารถระบุได้"))
            }
        }
    }
}

data class AuthError(
    val code: Int,
    val message: String,
    val redirect: String?
)
