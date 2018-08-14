package ffc.v3.authen

import ffc.entity.Organization
import ffc.v3.api.FfcCentral
import ffc.v3.api.OrgService
import okhttp3.Credentials
import retrofit2.dsl.enqueue
import retrofit2.dsl.then
import java.nio.charset.Charset

class LoginInteractor() {

    lateinit var loginPresenter: LoginPresenter
    lateinit var idRepo: IdentityRepo
    private val orgService = FfcCentral().service<OrgService>()

    var org: Organization? = null

    private val utf8 = Charset.forName("UTF-8")

    fun doLogin(username: String, password: String) {
        val basicToken = Credentials.basic(username.trim(), password.trim(), utf8)
        orgService.createAuthorize(org!!.id, basicToken).enqueue {
            onSuccess {
                val authorize = body()!!
                FfcCentral.TOKEN = authorize.token
                idRepo.org = org
                idRepo.token = authorize.token
                loginPresenter.onLoginSuccess()
            }
            onError {
                loginPresenter.onError(LoginErrorException())
            }
            onFailure {
                loginPresenter.onError(LoginFailureException(it.message ?: "Something wrong"))
            }
        }
    }

    fun requestMyOrg(callback: (List<Organization>, Throwable?) -> Unit) {
        orgService.myOrg().then {
            callback(it, null)
        }.catch { res, t ->
            res?.let {
                if (it.code() == 404)
                    // User doesn't connect to the hospital's network
                    callback(listOf(), null)
            }
            t?.let {
                callback(listOf(), LoginFailureException(it.message ?: "Something wrong"))
            }
        }
    }

    fun requestAllOrg(callback: (List<Organization>, Throwable?) -> Unit) {
        orgService.listOrgs().then {
            callback(it, null)
        }.catch { res, t ->
            res?.let {
                callback(listOf(), null)
            }
            t?.let {
                loginPresenter.onError(
                    LoginFailureException(it.message ?: "Something wrong")
                )
            }
        }
    }
}
