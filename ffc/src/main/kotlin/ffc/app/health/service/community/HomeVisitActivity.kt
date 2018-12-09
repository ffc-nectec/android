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

package ffc.app.health.service.community

import android.os.Bundle
import android.util.Log
import ffc.android.disable
import ffc.android.enable
import ffc.android.find
import ffc.android.onClick
import ffc.android.tag
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.auth.auth
import ffc.app.dev
import ffc.app.health.BodyFormFragment
import ffc.app.photo.TakePhotoFragment
import ffc.app.health.VitalSignFormFragment
import ffc.app.health.diagnosis.DiagnosisFormFragment
import ffc.app.health.service.healthCareServicesOf
import ffc.app.person.mockPerson
import ffc.app.person.personId
import ffc.app.person.persons
import ffc.app.util.alert.handle
import ffc.app.util.alert.toast
import ffc.entity.User
import ffc.entity.gson.toJson
import ffc.entity.healthcare.HealthCareService
import ffc.entity.util.generateTempId
import kotlinx.android.synthetic.main.activity_visit.done

class HomeVisitActivity : FamilyFolderActivity() {

    private val homeVisit by lazy { supportFragmentManager.find<HomeVisitFormFragment>(R.id.homeVisit) }
    private val vitalSign by lazy { supportFragmentManager.find<VitalSignFormFragment>(R.id.vitalSign) }
    private val diagnosis by lazy { supportFragmentManager.find<DiagnosisFormFragment>(R.id.diagnosis) }
    private val body by lazy { supportFragmentManager.find<BodyFormFragment>(R.id.body) }
    private val photo by lazy { supportFragmentManager.find<TakePhotoFragment>(R.id.photos) }

    private val providerId by lazy { auth(this).user!!.id }
    private val personId get() = intent.personId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)

        dev {
            if (auth(this).user == null) {
                auth(this).user = User(
                    generateTempId(),
                    "hello",
                    "world",
                    User.Role.PROVIDER, User.Role.SURVEYOR)
            }
            if (personId == null)
                intent.personId = mockPerson.id
        }

        persons(org!!.id).person(personId!!) {
            onFound {
                toast("visit ${it.name}")
            }
            onNotFound {
                toast("ไม่พบ")
                finish()
            }
            onFail {
                handle(it)
                finish()
            }
        }

        done.onClick { done ->
            try {
                val visit = HealthCareService(providerId, personId!!)
                homeVisit.dataInto(visit)
                vitalSign.dataInto(visit)
                diagnosis.dataInto(visit)
                body.dataInto(visit)
                photo.dataInto(visit)

                done.disable()
                healthCareServicesOf(personId!!, org!!.id).add(visit) {
                    onComplete {
                        dev { Log.d(tag, it.toJson()) }
                        toast("บันทึกข้อมูลเรียบร้อย")
                        finish()
                    }
                    onFail {
                        done.enable()
                        toast(it.message ?: "Something went wrong")
                    }
                }
            } catch (invalid: IllegalStateException) {
                handle(invalid)
                done.enable()
            } catch (throwable: Throwable) {
                handle(throwable)
                done.enable()
            }
        }
    }
}