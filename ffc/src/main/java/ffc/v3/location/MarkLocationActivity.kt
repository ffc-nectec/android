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

package ffc.v3.location

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import ffc.entity.Place
import ffc.entity.gson.ffcGson
import ffc.entity.gson.parseTo
import ffc.entity.gson.toJson
import ffc.v3.BaseActivity
import ffc.v3.R
import ffc.v3.R.id
import ffc.v3.R.layout
import ffc.v3.api.FfcCentral
import ffc.v3.api.PlaceService
import ffc.v3.util.find
import ffc.v3.util.toPoint
import kotlinx.android.synthetic.main.activity_add_location.done
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.support.v4.dimen
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.dsl.enqueue
import th.or.nectec.marlo.PointMarloFragment

class MarkLocationActivity : BaseActivity() {

    val REQ_TARGET = 10293

    lateinit var targetPlace: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layout.activity_add_location)

        val mapFragment = supportFragmentManager.find<PointMarloFragment>(id.mapFragment)
        with(mapFragment) {
            setPaddingTop(dimen(R.dimen.maps_padding_top))
            setMaxPoint(1)
            setStartLocation(
                intent.getParcelableExtra("target") as LatLng,
                intent.getFloatExtra("zoom", 15.0f)
            )
            onMapReady {
                findViewById(R.id.marlo_undo).visibility = View.GONE
            }
            setOnPointChange {
                if (it.isEmpty())
                    toast("Please Mark location")
                else {
                    this@MarkLocationActivity.done.show()
                    with(it[0].position) {
                        targetPlace.location = toPoint()
                    }
                }
            }
        }
        done.setOnClickListener {
            updateHouse()
        }
        done.hide()
        startActivityForResult<HouseNoLocationActivtiy>(REQ_TARGET)
    }

    private fun updateHouse() {
        val dialog = indeterminateProgressDialog("updating")

        FfcCentral().service<PlaceService>().updateHouse(org.id, targetPlace).enqueue {
            always { dialog.dismiss() }
            onSuccess {
                setResult(Activity.RESULT_OK)
                finish()
            }
            onError {
                alert("error ${code()}") {
                    positiveButton("try again") { updateHouse() }
                }.show()
            }
            onFailure {
                alert("${it.message}") {
                    positiveButton("try again") { updateHouse() }
                }.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                targetPlace = data?.getExtra<Place>("house")!!
                toast("targeting ${targetPlace.toJson()}")
            }
            Activity.RESULT_CANCELED -> finish()
        }
    }
}

inline fun <reified T> Intent.getExtra(key: String, gson: Gson = ffcGson): T? {
    return getStringExtra(key).parseTo<T>(gson)
}
