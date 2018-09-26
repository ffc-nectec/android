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
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPointStyle
import ffc.android.drawable
import ffc.android.toBitmap
import ffc.api.FfcCentral
import ffc.app.BuildConfig
import ffc.app.HouseActivity
import ffc.app.R
import ffc.app.familyFolderActivity
import ffc.entity.gson.toJson
import me.piruin.geok.geometry.Point
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.dimen
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import retrofit2.dsl.enqueue

class GeoMapsFragment : SupportMapFragment() {

    val REQ_ADD_LOCATION = 1032

    private lateinit var map: GoogleMap

    private var addLocationButton: FloatingActionButton? = null

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)

        addLocationButton = activity!!.find(R.id.addLocationButton)

        getMapAsync { googleMap ->
            map = googleMap.apply {
                setPadding(0, dimen(R.dimen.maps_padding_top), 0, 0)
            }
            addLocationButton?.setOnClickListener {
                val intent = intentFor<MarkLocationActivity>(
                    "target" to map.cameraPosition.target,
                    "zoom" to map.cameraPosition.zoom
                )
                startActivityForResult(intent, REQ_ADD_LOCATION)
            }
            showGeoJson()
        }
    }



    private fun showGeoJson() {

        val placeService = FfcCentral().service<PlaceService>()
        placeService.listHouseGeoJson(familyFolderActivity.org!!.id).enqueue {

            onSuccess {
                val coordinates = (body()!!.features[0].geometry as Point).coordinates
                map.animateCameraTo(coordinates.latitude, coordinates.longitude, 10.0f)
                with(GeoJsonLayer(map, JSONObject(body()!!.toJson()))) {
                    features.forEach {
                        it.pointStyle = GeoJsonPointStyle().apply {
                            icon = if (it.getProperty("haveChronics") == "true")
                                chronicHomeIcon else homeIcon
                            title = "บ้านเลขที่ ${it.getProperty("no")}"
                            snippet = it.getProperty("coordinates")?.trimMargin()
                        }
                    }
                    setOnFeatureClickListener {
                        startActivityForResult(
                            intentFor<HouseActivity>("houseId" to it.getProperty("_id")),
                            REQ_ADD_LOCATION)
                    }
                    addLayerToMap()
                }
            }
            onError {
                toast("Not success get geoJson ${code()} ")
                if (BuildConfig.DEBUG) {
                    map.animateCameraTo(13.0, 102.1, 10.0f)
                    GeoJsonLayer(map, R.raw.place, context)
                }
            }

            onFailure {
                toast("${it.message}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_ADD_LOCATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    map.clear()
                    showGeoJson()
                }
            }
        }
    }

    private val homeIcon by lazy { BitmapDescriptorFactory.fromBitmap(context!!.drawable(R.drawable.ic_home_black_24px).toBitmap()) }

    private val chronicHomeIcon by lazy { BitmapDescriptorFactory.fromBitmap(context!!.drawable(R.drawable.ic_home_red_24px).toBitmap()) }
}
