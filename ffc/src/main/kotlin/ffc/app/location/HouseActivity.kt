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

package ffc.app.location

import android.app.Activity
//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
//import android.support.v7.widget.LinearLayoutManager
import android.transition.Slide
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import ffc.android.allowTransitionOverlap
import ffc.android.enter
import ffc.android.excludeSystemView
import ffc.android.exit
import ffc.android.load
import ffc.android.observe
import ffc.android.setTransition
import ffc.android.viewModel
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.auth.auth
import ffc.app.dev
import ffc.app.person.PersonAdapter
import ffc.app.person.startPersonActivityOf
import ffc.app.photo.PhotoType
import ffc.app.photo.REQUEST_TAKE_PHOTO
import ffc.app.photo.startTakePhotoActivity
import ffc.app.photo.urls
import ffc.app.util.Analytics
import ffc.app.util.alert.handle
import ffc.app.util.alert.toast
import ffc.entity.Person
import ffc.entity.User
import ffc.entity.gson.toJson
import ffc.entity.place.House
import ffc.entity.update
import ffc.entity.util.URLs
import ffc.entity.util.generateTempId
import kotlinx.android.synthetic.main.activity_house.appbar
import kotlinx.android.synthetic.main.activity_house.collapsingToolbar
import kotlinx.android.synthetic.main.activity_house.emptyView
import kotlinx.android.synthetic.main.activity_house.recycleView
import kotlinx.android.synthetic.main.activity_house.toolbarImage
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class HouseActivity : FamilyFolderActivity() {

    val houseId: String?
        get() = intent.getStringExtra("houseId")

    lateinit var viewModel: HouseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house)
        setTransition {
            enterTransition = Slide(Gravity.BOTTOM).enter().excludeSystemView()
            exitTransition = Slide(Gravity.START).exit()
            reenterTransition = Slide(Gravity.START).enter()
            allowTransitionOverlap = false
        }

        dev {
            if (houseId == null) intent.putExtra("houseId", generateTempId())
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "บ้านเลขที่ ?"

        viewModel = viewModel()
        observe(viewModel.house) { house ->
            if (house != null) {
                bind(house)
                house.resident(org!!.id) {
                    onFound { viewModel.resident.value = it }
                    onNotFound { viewModel.resident.value = null }
                    onFail { viewModel.exception.value = it }
                }
                Analytics.instance?.view(house)
            } else {
                emptyView.error(Error("ไม่พบข้อมูล")).show()
                finish()
            }
            photoMenu?.isEnabled = house != null
            locationMenu?.isEnabled = house != null
        }
        observe(viewModel.resident) {
            if (!it.isNullOrEmpty()) {
                (recycleView.adapter as PersonAdapter).update(it.sortedByDescending { it.age })
                emptyView.content().show()
            } else {
                emptyView.empty().show()
            }
        }
        observe(viewModel.exception) { t ->
            t?.let {
                handle(it)
                emptyView.error(it).show()
            }
        }
        emptyView.loading().show()

        with(recycleView) {
            layoutManager = LinearLayoutManager(context)
            adapter = PersonAdapter(listOf()) {
                onItemClick {
                    startPersonActivityOf(it, viewModel.house.value,
                        appbar to getString(R.string.transition_appbar),
                        find<ImageView>(R.id.personImageView) to getString(R.string.transition_person_profile)
                    )
                }
            }
        }
    }

    fun bind(house: House) {
        collapsingToolbar!!.title = "บ้านเลขที่ ${house.no}"
        supportActionBar!!.title = "บ้านเลขที่ ${house.no}"
        house.avatarUrl?.let { toolbarImage.load(Uri.parse(it)) }
    }

    override fun onResume() {
        super.onResume()
        housesOf(org!!).house(houseId!!) {
            onFound { viewModel.house.value = it }
            onNotFound { viewModel.house.value = null }
            onFail { viewModel.exception.value = it }
        }
    }

    var photoMenu: MenuItem? = null
    var locationMenu: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.house_option, menu)
        photoMenu = menu.findItem(R.id.photoMenu)
        photoMenu!!.isEnabled = false
        locationMenu = menu.findItem(R.id.locationMenu)
        locationMenu!!.isEnabled = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.photoMenu -> startTakePhotoActivity(PhotoType.PLACE, viewModel.house.value?.imagesUrl)
            R.id.locationMenu -> startActivity<MarkLocationActivity>("house" to viewModel.house.value?.toJson())
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.house.value?.update {
                        imagesUrl = data!!.urls!!.mapTo(URLs()) { it }
                    }?.pushTo(org!!) {
                        onComplete {
                            toast("ปรับปรุงข้อมูลแล้ว")
                            viewModel.house.value = it
                        }
                        onFail { handle(it) }
                    }
                }
            }
        }
    }

    class HouseViewModel : ViewModel() {
        var house: MutableLiveData<House> = MutableLiveData()
        var resident: MutableLiveData<List<Person>> = MutableLiveData()
        var exception: MutableLiveData<Throwable> = MutableLiveData()
    }
}
