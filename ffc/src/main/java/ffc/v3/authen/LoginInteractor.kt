package ffc.v3.authen

import android.content.Context
import android.util.Log
import ffc.entity.Organization
import ffc.v3.R
import ffc.v3.api.FfcCentral
import ffc.v3.api.OrgService
import okhttp3.Credentials
import retrofit2.dsl.enqueue
import retrofit2.dsl.then
import java.nio.charset.Charset
import ffc.v3.authen.LoginPresenter

class LoginInteractor() {

    lateinit var loginPresenter : LoginPresenter
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
                loginPresenter.onLoginSuccess()

                idRepo.org = org
                idRepo.token = authorize.token
            }
            onError {
                loginPresenter.onError(R.string.identification_error)
            }
            onFailure {
                loginPresenter.onError(it.message ?: "Something wrong")
            }
        }
    }

    fun requestMyOrg(callback: (MutableList<Any>) -> Unit) {
        val myList: MutableList<Any> = mutableListOf<Any>()

        orgService.myOrg().then {
            myList.add(0, "Success")
            myList.add(1, it)
            callback(myList)
        }.catch { res, t ->
            if (res!!.code() == 404)
                myList.add(0, 404)
            callback(myList)
//            res?.let { presenter.onError("Error") }
//            t?.let { presenter.onError(it.message ?: "Something wrong") }
        }
    }
}
