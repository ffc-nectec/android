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
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import ffc.android.allowTransitionOverlap
import ffc.android.enter
import ffc.android.exit
import ffc.android.load
import ffc.android.onClick
import ffc.android.sceneTransition
import ffc.android.setTransition
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.healthservice.HealthCareServicesFragment
import ffc.app.healthservice.HomeVisitActivity
import ffc.app.isDev
import ffc.app.location.HouseActivity
import ffc.app.photo.PhotoType
import ffc.app.photo.REQUEST_TAKE_PHOTO
import ffc.app.photo.startAvatarPhotoActivity
import ffc.entity.Person
import ffc.entity.update
import kotlinx.android.synthetic.main.activity_person.ageView
import kotlinx.android.synthetic.main.activity_person.avatarView
import kotlinx.android.synthetic.main.activity_person.homeAsUp
import kotlinx.android.synthetic.main.activity_person.nameView
import kotlinx.android.synthetic.main.activity_person.visitButton
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class PersonActivitiy : FamilyFolderActivity() {

    val personId get() = intent.personId!!
    lateinit var person: Person
    val startFromActivity get() = intent.getStringExtra("starter")

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

        if (savedInstanceState == null) {
            val fragment = HealthCareServicesFragment()
            fragment.arguments = bundleOf("personId" to personId)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.contentContainer, fragment)
                .commit()
        }

        visitButton.onClick {
            startActivity<HomeVisitActivity>("personId" to personId)
        }
    }

    override fun onResume() {
        super.onResume()

        persons(org!!.id).person(personId) {
            onFound { bind(it) }
            onNotFound {
                toast("Not found")
                finish()
            }
        }
    }

    private fun bind(person: Person) {
        this.person = person
        with(person) {
            nameView.text = name
            age?.let { ageView.text = "อายุ $it ปี" }
            avatarUrl?.let { url -> avatarView.load(Uri.parse(url)) }
            avatarView.onClick {
                startAvatarPhotoActivity(PhotoType.PERSON, avatarUrl, it)
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
                    person.update { avatarUrl = uri.toString() }
                    //TODO put to api
                }
            }
        }
    }
}

fun Activity.startPersonActivityOf(person: Person, vararg sharedElements: Pair<View, String>?) {
    val intent = intentFor<PersonActivitiy>(
        "personId" to person.id,
        "starter" to when (this) {
            is HouseActivity -> HouseActivity::class.java.name
            else -> null
        })
    startActivity(intent, sceneTransition(*sharedElements))
}
