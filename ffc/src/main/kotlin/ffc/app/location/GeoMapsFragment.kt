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
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.design.widget.FloatingActionButton
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPointStyle
import com.sembozdemir.permissionskt.handlePermissionsResult
import ffc.android.drawable
import ffc.android.get
import ffc.android.gone
import ffc.android.put
import ffc.android.sceneTransition
import ffc.android.toBitmap
import ffc.app.R
import ffc.app.dev
import ffc.app.familyFolderActivity
import ffc.app.util.alert.handle
import ffc.entity.gson.toJson
import me.piruin.geok.geometry.Point
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.intentFor
import org.json.JSONObject
import th.or.nectec.marlo.PointMarloFragment

class GeoMapsFragment : PointMarloFragment() {

    val REQ_ADD_LOCATION = 1032

    private var addLocationButton: FloatingActionButton? = null

    private var lastCameraPosition: CameraPosition?
        set(value) {
            preferences.edit().put("campos", value).apply()
        }
        get() = preferences.get("campos")

    private val preferences: SharedPreferences by lazy { context!!.getSharedPreferences("geomap", Context.MODE_PRIVATE) }

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)

        setStartLocation(lastCameraPosition)
        addLocationButton = activity!!.find(R.id.addLocationButton)
        viewFinder.gone()
        hideToolsMenu()
    }

    private fun GeoMapsFragment.setStartLocation(lastPosition: CameraPosition?) {
        if (lastPosition != null) {
            setStartLocation(lastPosition.target, lastPosition.zoom)
        } else {
            setStartLocation(LatLng(13.76498, 100.538335), 5.0f)
            setStartAtCurrentLocation(true)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        super.onMapReady(googleMap)

        addLocationButton?.setOnClickListener {
            val intent = intentFor<MarkLocationActivity>(
                "target" to googleMap!!.cameraPosition.target,
                "zoom" to googleMap.cameraPosition.zoom
            )
            startActivityForResult(intent, REQ_ADD_LOCATION)
        }
        showGeoJson()
        askMyLocationPermission()
    }

    private fun showGeoJson() {
        placeGeoJson(familyFolderActivity.org!!).all {
            onFound {
                val coordinates = (it.features[0].geometry as Point).coordinates
                googleMap.animateCameraTo(coordinates.latitude, coordinates.longitude)
                addGeoJsonLayer(GeoJsonLayer(googleMap, JSONObject(it.toJson())))
            }
            onFail {
                dev {
                    googleMap.animateCameraTo(13.0, 102.1, 12.0f)
                    addGeoJsonLayer(GeoJsonLayer(googleMap, R.raw.place, context))
                }
                familyFolderActivity.handle(it)
            }
        }
    }

    fun addGeoJsonLayer(layer: GeoJsonLayer) {
        with(layer) {
            features.forEach {
                it.pointStyle = GeoJsonPointStyle().apply {
                    icon = if (it.getProperty("haveChronic") == "true")
                        chronicHomeIcon else homeIcon
                    title = "บ้านเลขที่ ${it.getProperty("no")}"
                    snippet = it.getProperty("coordinates")?.trimMargin()
                }
            }
            setOnFeatureClickListener {
                val intent = intentFor<HouseActivity>("houseId" to it.getProperty("id"))
                startActivityForResult(intent, REQ_ADD_LOCATION, activity!!.sceneTransition())
            }
            addLayerToMap()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_ADD_LOCATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    googleMap.clear()
                    showGeoJson()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        activity!!.handlePermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStop() {
        super.onStop()
        googleMap?.cameraPosition?.let { lastCameraPosition = it }
    }

    fun bitmapOf(@DrawableRes resId: Int) = BitmapDescriptorFactory.fromBitmap(context!!.drawable(resId).toBitmap())

    private val homeIcon by lazy { bitmapOf(R.drawable.ic_marker_home_green_24dp) }

    private val chronicHomeIcon by lazy { bitmapOf(R.drawable.ic_marker_home_red_24dp) }
}
