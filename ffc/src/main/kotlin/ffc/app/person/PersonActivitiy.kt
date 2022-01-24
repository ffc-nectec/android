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

package ffc.app.person

import android.app.Activity
//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ffc.android.allowTransitionOverlap
import ffc.android.enter
import ffc.android.exit
import ffc.android.load
import ffc.android.observe
import ffc.android.onClick
import ffc.android.replaceAll
import ffc.android.sceneTransition
import ffc.android.setTransition
import ffc.android.viewModel
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.auth.auth
import ffc.app.health.analyze.HealthIssueFragment
import ffc.app.health.service.HealthCareServicesFragment
import ffc.app.health.service.community.HomeVisitActivity
import ffc.app.isDev
import ffc.app.location.HouseActivity
import ffc.app.person.genogram.GenogramActivity
import ffc.app.person.genogram.personPopupActivity
import ffc.app.photo.PhotoType
import ffc.app.photo.REQUEST_TAKE_PHOTO
import ffc.app.photo.startAvatarPhotoActivity
import ffc.app.search.RecentPersonProvider
import ffc.app.util.Analytics
import ffc.app.util.alert.handle
import ffc.app.util.alert.toast
import ffc.entity.Person
import ffc.entity.User
import ffc.entity.place.House
import ffc.entity.update
import kotlinx.android.synthetic.main.activity_person.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class PersonActivitiy : FamilyFolderActivity() {

    val personId get() = intent.personId!!
    val startFromActivity get() = intent.getStringExtra("starter")

    lateinit var viewModel: PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTransition {
            enterTransition = Slide(Gravity.END).enter()
            returnTransition = Slide(Gravity.END).exit()
            allowTransitionOverlap = false
        }

        if (isDev && intent.personId == null) {
            intent.personId = mockPerson.id
        }

        with(intent) {
            getStringExtra("avatarUrl")?.let { url -> avatarView.load(Uri.parse(url)) }
            getStringExtra("name")?.let { nameView.text = it }
            getStringExtra("age")?.let { ageView.text = "อายุ $it ปี" }
            getStringExtra("houseAvatarUrl")?.let { url -> toolbarImage.load((Uri.parse(url))) }
        }

        visitButton.onClick {
            startActivity<HomeVisitActivity>("personId" to personId)
        }
        genogramButton.onClick {
            startActivity<GenogramActivity>("personId" to personId)
        }
        genogramButton.hide()

        viewModel = viewModel()
        observe(viewModel.person) {
            if (it != null) {
                bind(it)
                Analytics.instance?.view(it)
            } else {
                toast("ไม่พบข้อมูล")
                finish()
            }
        }
        observe(viewModel.exception) {
            it?.let { handle(it) }
        }
        var user = auth(this).user
        if (user!!.roles[0] == User.Role.SURVEYOR ){
            btnInfo.visibility =  View.GONE
        }
        btnInfo.onClick {

            var intent =  Intent(this, personPopupActivity::class.java)
            //intent.putExtra("id", id)
            intent.putExtra("personId", personId)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        persons(org!!.id).person(personId) {
            onFound { viewModel.person.value = it }
            onNotFound { viewModel.person.value = null }
            onFail { viewModel.exception.value = it }
        }
    }

    private fun bind(person: Person) {
        RecentPersonProvider(this, org!!).saveRecentPerson(person)
        with(person) {
            nameView.text = name
            age?.let { ageView.text = "อายุ $it ปี" }
            deadLabelView.visibility = if (isDead) View.VISIBLE else View.GONE
            avatarView.onClick {
                startAvatarPhotoActivity(PhotoType.PERSON, avatarUrl, it)
            }
            val fragmentAdd = mutableMapOf<String, Fragment>()
            if (isDead) {
                visitButton.hide()

                val deathFragment = DeathFragment()
                deathFragment.death = death
                fragmentAdd.put("death", deathFragment)
            }

            if (person.relationships.isNotEmpty()) {
                val relationshipFragment = RelationshipFragment()
                relationshipFragment.person = this
                fragmentAdd.put("relationship", relationshipFragment)
            }
            val user = auth(applicationContext).user!!
            if(user.roles.size>0) {
                if (user.roles[0] != User.Role.SURVEYOR) {
                    val issueFragment = HealthIssueFragment()
                    issueFragment.person = this
                    fragmentAdd.put("Issue", issueFragment)
                    val serviceFragment = HealthCareServicesFragment()
                    serviceFragment.arguments = bundleOf("personId" to personId)
                    fragmentAdd.put("service", serviceFragment)
                }
            }
            supportFragmentManager
                .replaceAll(R.id.contentContainer, fragmentAdd)
                .commit()
        }
        homeAsUp.onClick {
            if (startFromActivity == HouseActivity::class.java.name) {
                onBackPressed()
            } else {
                startActivity<HouseActivity>("houseId" to person.houseId)
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data!!.data!!
                    avatarView.load(uri)
                    viewModel.person.value?.update { avatarUrl = uri.toString() }?.pushTo(org!!) {
                        onComplete { viewModel.person.value = it }
                        onFail { viewModel.exception.value = it }
                    }
                }
            }
        }
    }

    class PersonViewModel : ViewModel() {
        val person: MutableLiveData<Person> = MutableLiveData()
        val exception: MutableLiveData<Throwable> = MutableLiveData()
    }
}

fun Activity.startPersonActivityOf(person: Person, house: House? = null, vararg sharedElements: Pair<View, String>?) {
    val intent = intentFor<PersonActivitiy>(
        "personId" to person.id,
        "avatarUrl" to person.avatarUrl,
        "name" to person.name,
        "age" to person.age,
        "starter" to when (this) {
            is HouseActivity -> HouseActivity::class.java.name
            else -> null
        })
    house?.avatarUrl?.let { intent.putExtra("houseAvatarUrl", it) }
    startActivity(intent, sceneTransition(*sharedElements))
}
