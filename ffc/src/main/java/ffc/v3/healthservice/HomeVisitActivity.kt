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

    internal val homeVisit by lazy { supportFragmentManager.find<HomeServiceFormFragment>(R.id.homeVisit) }
    internal val vitalSign by lazy { supportFragmentManager.find<VitalSignFormFragment>(R.id.vitalSign) }
    internal val diagnosis by lazy { supportFragmentManager.find<DiagnosisFormFragment>(R.id.diagnosis) }

    val providerId by lazy { getIdentityRepo(this).user!!.id }
    val personId get() = intent.getStringExtra("personId")

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

        done.onClick { _ ->
            try {
                val visit = HomeVisit(providerId, personId, notDefineCommunityService)
                homeVisit.dataInto(visit)
                vitalSign.dataInto(visit)
                diagnosis.dataInto(visit)

                persons().person(personId) { p, _ ->
                    p!!.healthCareServices().add(visit) { s, t ->
                        t?.let { throw it }
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


}
