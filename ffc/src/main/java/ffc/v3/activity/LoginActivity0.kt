package ffc.v3.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import ffc.entity.Organization
import ffc.entity.Token
import ffc.v3.BaseActivity
import ffc.v3.MapsActivity
import ffc.v3.R
import ffc.v3.api.FfcCentral
import ffc.v3.api.OrgService
import ffc.v3.authen.LoginInteractor
import ffc.v3.authen.LoginPresenter
import ffc.v3.authen.OrgSelectFragment
import ffc.v3.authen.UserPassFragment
import ffc.v3.util.debugToast
import ffc.v3.util.get
import me.piruin.spinney.Spinney
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.intentFor

class LoginActivity0 : BaseActivity(), LoginPresenter {

    private val organization by lazy { findViewById<Spinney<Organization>>(R.id.orgView) }
    private val orgService = FfcCentral().service<OrgService>()

    lateinit var interactor: LoginInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val authorize = defaultSharedPreferences.get<Token>("token")
        if (authorize?.isNotExpire == true) {
            debugToast("Use last token")
            FfcCentral.TOKEN = authorize.token.toString()
            startActivity(intentFor<MapsActivity>())
            finish()
            return
        }

        val controller = LoginInteractor()
//        controller.presenter = this

        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, getOrgFragmentt(), "org")
            .addToBackStack(null)
            .commit()

        controller.requestMyOrg {
            //            val fragment = supportFragmentManager.findFragmentByTag("tag") as OrgSelectFragment
        }
    }

    private fun getOrgFragmentt(): Fragment {
        return OrgSelectFragment().apply {
            onNext = {
                interactor.org = it
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, getUserPassFragment(org))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun getUserPassFragment(org: Organization): Fragment {
        return UserPassFragment().apply {
            organization = org
//            onLoginRequest = { user, pass ->
//                interactor.doLogin(user, pass)
//            }
        }
    }

    override fun onLoginSuccess() {
        startActivity(intentFor<MapsActivity>())
    }

    override fun onError(message: Int) {
    }

    override fun onError(message: String) {
    }
}
