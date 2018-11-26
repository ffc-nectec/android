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
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.Slide
import android.view.Gravity
import android.view.View
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
import ffc.app.healthservice.HealthCareServicesFragment
import ffc.app.healthservice.HealthIssueFragment
import ffc.app.healthservice.HomeVisitActivity
import ffc.app.healthservice.healthIssues
import ffc.app.isDev
import ffc.app.location.HouseActivity
import ffc.app.person.genogram.GenogramActivity
import ffc.app.photo.PhotoType
import ffc.app.photo.REQUEST_TAKE_PHOTO
import ffc.app.photo.startAvatarPhotoActivity
import ffc.app.util.alert.handle
import ffc.app.util.alert.toast
import ffc.entity.Person
import ffc.entity.place.House
import ffc.entity.update
import kotlinx.android.synthetic.main.activity_person.ageView
import kotlinx.android.synthetic.main.activity_person.avatarView
import kotlinx.android.synthetic.main.activity_person.deadLabelView
import kotlinx.android.synthetic.main.activity_person.genogramButton
import kotlinx.android.synthetic.main.activity_person.homeAsUp
import kotlinx.android.synthetic.main.activity_person.nameView
import kotlinx.android.synthetic.main.activity_person.toolbarImage
import kotlinx.android.synthetic.main.activity_person.visitButton
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
            } else {
                toast("ไม่พบข้อมูล")
                finish()
            }
        }
        observe(viewModel.exception) {
            it?.let { handle(it) }
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
        with(person) {
            nameView.text = name
            age?.let { ageView.text = "อายุ $it ปี" }
            deadLabelView.visibility = if (isDead) View.VISIBLE else View.GONE
            avatarView.onClick {
                startAvatarPhotoActivity(PhotoType.PERSON, avatarUrl, it)
            }
            val fragmentAdd = mutableMapOf<String, Fragment>()
            if (isDead) {
                val deathFragment = DeathFragment()
                deathFragment.death = death
                fragmentAdd.put("death", deathFragment)
            }
            val relationshipFragment = RelationshipFragment()
            relationshipFragment.person = this
            fragmentAdd.put("relationship", relationshipFragment)

            val fragment = HealthCareServicesFragment()
            fragment.arguments = bundleOf("personId" to personId)
            fragmentAdd.put("service", fragment)

            supportFragmentManager
                .replaceAll(R.id.contentContainer, fragmentAdd)
                .commit()
        }

        healthIssues().issueOf(person) {
            onFound {
                val fragment = HealthIssueFragment()
                fragment.issues = it
                supportFragmentManager
                    .replaceAll(R.id.contentContainer, "issue" to fragment)
                    .commit()
            }
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
