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

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import ffc.android.disable
import ffc.android.enable
import ffc.android.find
import ffc.android.getExtra
import ffc.android.loadDrawableBottom
import ffc.android.observe
import ffc.android.onClick
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.auth.auth
import ffc.app.dev
import ffc.app.health.BodyFormFragment
import ffc.app.health.VitalSignFormFragment
import ffc.app.health.diagnosis.DiagnosisFormFragment
import ffc.app.health.service.healthCareServicesOf
import ffc.app.person.mockPerson
import ffc.app.person.personId
import ffc.app.person.persons
import ffc.app.photo.TakePhotoFragment
import ffc.app.util.SimpleViewModel
import ffc.app.util.TaskCallback
import ffc.app.util.alert.handle
import ffc.app.util.alert.toast
import ffc.entity.Person
import ffc.entity.User
import ffc.entity.gson.toJson
import ffc.entity.healthcare.HealthCareService
import ffc.entity.update
import ffc.entity.util.generateTempId
import kotlinx.android.synthetic.main.activity_visit.done
import kotlinx.android.synthetic.main.activity_visit.personName
import timber.log.Timber

class HomeVisitActivity : FamilyFolderActivity() {

    private val homeVisit by lazy { supportFragmentManager.find<HomeVisitFormFragment>(R.id.homeVisit) }
    private val vitalSign by lazy { supportFragmentManager.find<VitalSignFormFragment>(R.id.vitalSign) }
    private val diagnosis by lazy { supportFragmentManager.find<DiagnosisFormFragment>(R.id.diagnosis) }
    private val body by lazy { supportFragmentManager.find<BodyFormFragment>(R.id.body) }
    private val photo by lazy { supportFragmentManager.find<TakePhotoFragment>(R.id.photos) }

    private val providerId by lazy { auth(this).user!!.id }
    private val personId get() = intent.personId
    private val service get() = intent.getExtra<HealthCareService>("service")

    private val viewModel by lazy { SimpleViewModel<Person>() }

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

        setupPersonInfo()

        service?.let {
            Timber.d("Edit visit=${it.toJson()}")
            homeVisit.bind(it)
            vitalSign.bind(it)
            diagnosis.bind(it)
            body.bind(it)
            photo.bind(it)
        }

        if (service == null) {
            diagnosis.addDefaultPrincipleDx()
        }

        done.onClick { done ->
            try {
                val visit = service ?: HealthCareService(providerId, personId!!)
                visit.update {
                    homeVisit.dataInto(visit)
                    vitalSign.dataInto(visit)
                    diagnosis.dataInto(visit)
                    body.dataInto(visit)
                    photo.dataInto(visit)
                }
                Timber.d("visit=%s", visit.toJson())

                done.disable()
                val healthCareServices = healthCareServicesOf(personId!!, org!!.id)
                if (visit.isTempId)
                    healthCareServices.add(visit, callback)
                else
                    healthCareServices.update(visit, callback)
            } catch (invalid: IllegalStateException) {
                handle(invalid)
                done.enable()
            } catch (throwable: Throwable) {
                handle(throwable)
                done.enable()
            }
        }
    }

    val callback: TaskCallback<HealthCareService>.() -> Unit = {
        onComplete {
            Timber.d("post/put service = %s", it.toJson())
            toast("บันทึกข้อมูลเรียบร้อย")
            setResult(Activity.RESULT_OK)
            finish()
        }
        onFail {
            done.enable()
            toast(it.message ?: "Something went wrong")
        }
    }

    private fun setupPersonInfo() {
        observe(viewModel.content) {
            if (it != null) {
                personName.text = it.name
                it.avatarUrl?.let { personName.loadDrawableBottom(Uri.parse(it)) }
            } else {
                toast("ไม่พบข้อมูลบุคคล")
                finish()
            }
        }
        observe(viewModel.exception) {
            it?.let {
                handle(it)
                finish()
            }
        }
        persons(org!!.id).person(personId!!) {
            onFound { viewModel.content.value = it }
            onNotFound { viewModel.content.value = null }
            onFail { viewModel.exception.value = it }
        }
    }
}
