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
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.design.widget.FloatingActionButton
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.data.geojson.GeoJsonFeature
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPointStyle
import ffc.android.drawable
import ffc.android.observe
import ffc.android.rawAs
import ffc.android.sceneTransition
import ffc.android.toBitmap
import ffc.android.viewModel
import ffc.app.R
import ffc.app.dev
import ffc.app.familyFolderActivity
import ffc.app.location.GeoMapsInfo.ELDER
import ffc.app.util.alert.handle
import ffc.app.util.forEachChunk
import ffc.app.util.md5
import ffc.entity.gson.toJson
import ffc.entity.place.House
import me.piruin.geok.geometry.FeatureCollection
import me.piruin.geok.geometry.Point
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.onUiThread
import org.json.JSONObject
import timber.log.Timber

class GeoMapsFragment : BaseMapsFragment() {

    val REQ_ADD_LOCATION = 1032

    val preferZoomLevel = 17.0f

    private var addLocationButton: FloatingActionButton? = null
    private val viewModel by lazy { viewModel<GeoViewModel>() }

    lateinit var markerStyles: MarkerStyles

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addLocationButton = activity!!.find(R.id.addLocationButton)

        observeViewModel()
        markerStyles = ChronicMarkerStyles(context!!)
    }

    private fun observeViewModel() {
        observe(viewModel.geojson) {
            it?.let {
                val coordinates = (it.features[0].geometry as Point).coordinates
                googleMap.animateCameraTo(
                    coordinates.latitude,
                    coordinates.longitude,
                    googleMap?.cameraPosition?.zoom?.takeIf { it >= preferZoomLevel }
                        ?: preferZoomLevel
                )
                googleMap.clear()
                viewModel.json.value = JSONObject(it.toJson())
                addGeoJsonLayer(GeoJsonLayer(googleMap, viewModel.json.value))
            }
        }
        observe(viewModel.exception) {
            it?.let { familyFolderActivity.handle(it) }
        }
    }



    override fun onMapReady(googleMap: GoogleMap?) {
        super.onMapReady(googleMap)
//        viewModel.geojson.value = doAsyncResult { preference.geojsonCache }.get()
//        addLocationButton?.setOnClickListener {
//            val intent = intentFor<MarkLocationActivity>(
//                "target" to googleMap!!.cameraPosition.target,
//                "zoom" to googleMap.cameraPosition.zoom
//            )
//            startActivityForResult(intent, REQ_ADD_LOCATION)
//        }
        loadGeoJson()
    }

    private fun loadGeoJson() {
        placeGeoJson(familyFolderActivity.org!!).all {
            onFound { viewModel.geojson.value = it }
            onFail {
                dev { viewModel.geojson.value = context?.rawAs(R.raw.place) }
                viewModel.exception.value = it
            }
        }
    }

    private fun addGeoJsonLayer(layer: GeoJsonLayer) {
        with(layer) {
            features.forEach { it.pointStyle = markerStyles.pointStyleOf(it) }
            setOnFeatureClickListener { feature ->
                val intent = intentFor<HouseActivity>("houseId" to feature.getProperty("id"))
                startActivityForResult(intent, REQ_ADD_LOCATION, activity!!.sceneTransition())
            }
            addLayerToMap()
        }
        viewModel.layer.value = layer
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_ADD_LOCATION -> {
                if (resultCode == Activity.RESULT_OK) loadGeoJson()
            }
        }
    }

    fun showInfo(info: GeoMapsInfo, onProgress: (Double) -> Unit) {
        markerStyles = when (info) {
            ELDER -> ElderMarkerStyle(context!!)
            else -> ChronicMarkerStyles(context!!)
        }

        viewModel.layer.value?.let { layer ->
            googleMap.uiSettings.setAllGesturesEnabled(false)
            doAsync {
                layer.features.forEachChunk(200, 150) { progress, list ->
                    onUiThread {
                        list.forEach {
                            it.pointStyle = markerStyles.pointStyleOf(it)
                        }
                        onProgress(progress)
                    }
                }
                Timber.d("finish")
                onUiThread { googleMap.uiSettings.setAllGesturesEnabled(true) }
            }
        }
    }


    class GeoViewModel : ViewModel() {
        val geojson = MutableLiveData<FeatureCollection<House>>()
        val json = MutableLiveData<JSONObject>()
        val layer = MutableLiveData<GeoJsonLayer>()
        val exception = MutableLiveData<Throwable>()
    }

    interface MarkerStyles {

        val context: Context

        fun bitmapOf(@DrawableRes resId: Int): BitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(context.drawable(resId).toBitmap())

        fun pointStyleOf(it: GeoJsonFeature): GeoJsonPointStyle
    }

    class ChronicMarkerStyles(override val context: Context) : MarkerStyles {

        private val homeIcon by lazy { bitmapOf(R.drawable.ic_marker_home_green_24dp) }

        private val chronicHomeIcon by lazy { bitmapOf(R.drawable.ic_marker_home_red_24dp) }

        override fun pointStyleOf(it: GeoJsonFeature): GeoJsonPointStyle {
            return GeoJsonPointStyle().apply {
                icon = if (it.getProperty("haveChronic") == "true") chronicHomeIcon else homeIcon
                title = "บ้านเลขที่ ${it.getProperty("no")}"
                snippet = it.getProperty("coordinates")?.trimMargin()
            }
        }
    }

    class ElderMarkerStyle(override val context: Context) : MarkerStyles {

        private val notElderIcon by lazy { bitmapOf(R.drawable.ic_marker_home_grey_24dp) }
        private val socialIcon by lazy { bitmapOf(R.drawable.ic_marker_home_green_24dp) }
        private val stayIcon by lazy { bitmapOf(R.drawable.ic_marker_home_yellow_24dp) }
        private val bedIcon by lazy { bitmapOf(R.drawable.ic_marker_home_red_24dp) }

        override fun pointStyleOf(it: GeoJsonFeature): GeoJsonPointStyle {
            return GeoJsonPointStyle().apply {
                val md5 = it.getProperty("no").md5().toLowerCase()
                icon = when (md5[0]) {
                    '1' -> bedIcon
                    '4', '5' -> stayIcon
                    '6', '8', 'a' -> socialIcon
                    else -> notElderIcon
                }
                title = md5
            }
        }
    }
}

