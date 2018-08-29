package ffc.v3.healthservice

import android.os.Bundle
import android.util.Log
import ffc.entity.User
import ffc.entity.gson.toJson
import ffc.entity.healthcare.HomeVisit
import ffc.entity.util.generateTempId
import ffc.v3.BaseActivity
import ffc.v3.BuildConfig
import ffc.v3.R
import ffc.v3.android.onClick
import ffc.v3.android.tag
import ffc.v3.android.toast
import ffc.v3.authen.getIdentityRepo
import ffc.v3.person.mockPerson
import ffc.v3.person.persons
import ffc.v3.util.find
import kotlinx.android.synthetic.main.activity_visit.done
import org.jetbrains.anko.toast

class HomeVisitActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)

        if (BuildConfig.DEBUG) {
            getIdentityRepo(this).user = User(
                generateTempId(),
                "hello",
                "world",
                User.Role.PROVIDER, User.Role.SURVEYOR)
            intent.putExtra("personId", mockPerson.id)
        }

        val homeVisit = supportFragmentManager.find<HomeServiceFormFragment>(R.id.homeVisit)
        val vitalSign = supportFragmentManager.find<VitalSignFormFragment>(R.id.vitalSign)

        done.onClick {
            try {
                val visit = HomeVisit(providerId, personId, notDefineCommunityService)
                homeVisit.dataInto(visit)
                vitalSign.dataInto(visit)

                persons().person(personId) { p, _ ->
                    p!!.healthCareServices().add(visit) { s, t ->
                        t?.let { throw t }
                        s?.let {
                            Log.d(tag, it.toJson())
                            toast("Services save")
                            finish()
                        }
                    }
                }
            } catch (invalid: IllegalStateException) {
                toast(invalid)
            } catch (throwable: Throwable) {
                toast(throwable)
            }
        }
    }

    val providerId by lazy { getIdentityRepo(this).user!!.id }

    val personId get() = intent.getStringExtra("personId")
}
