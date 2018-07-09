package ffc.v3.authen

import android.content.Context
import ffc.entity.Organization
import ffc.v3.R
import ffc.v3.api.FfcCentral
import ffc.v3.api.OrgService
import okhttp3.Credentials
import retrofit2.dsl.enqueue
import retrofit2.dsl.then
import java.nio.charset.Charset

class LoginController(val context: Context) {

    var idRepo = getIdentityRepo(context)
    private val orgService = FfcCentral().service<OrgService>()
    lateinit var presenter: LoginPresenter

    var org: Organization? = null

    private val utf8 = Charset.forName("UTF-8")

    fun doLogin(username: String, password: String) {
        val basicToken = Credentials.basic(username.trim(), password.trim(), utf8)

        orgService.createAuthorize(org!!.id.toLong(), basicToken).enqueue {
            onSuccess {
                presenter.onLoginSuccess {}
                val authorize = body()!!
                FfcCentral.TOKEN = authorize.token
                idRepo.org = org
                idRepo.token = authorize.token
            }
            onError {
                presenter.error(context.getString(R.string.identification_error))
            }
            onFailure {
                presenter.error(it.message ?: "Something wrong")
            }
        }
    }

    fun requestMyOrg(callback: (Organization) -> Unit) {
        orgService.myOrg().then {
            callback(it[0])
        }.catch { res, t ->
            res?.let { presenter.error("Error") }
            t?.let { presenter.error(it.message ?: "Something wrong") }
        }
    }
}
