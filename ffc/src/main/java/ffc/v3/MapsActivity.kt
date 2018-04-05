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

package ffc.v3

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPointStyle
import ffc.v3.api.FfcCentral
import ffc.v3.api.PlaceService
import ffc.v3.util.drawable
import ffc.v3.util.get
import ffc.v3.util.then
import ffc.v3.util.toBitmap
import ffc.v3.util.toJson
import me.piruin.geok.geometry.Point
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

  private lateinit var mMap: GoogleMap

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_maps)
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    val mapFragment = supportFragmentManager
      .findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
  }

  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    val org = defaultSharedPreferences.get<Org>("org")!!
    val call = FfcCentral().service<PlaceService>().listHouseGeoJson(org.id)

    call.then { res, t ->
      res?.let {
        val layer = if (!it.isSuccessful) {
          toast("Not success get geoJson ${it.code()} ")
          mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(13.0, 102.1), 10.0f))
          GeoJsonLayer(mMap, R.raw.place, this)
        } else {
          val body = it.body()!!
          val coordinates = (body.features[0].geometry as Point).coordinates
          mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
              LatLng(
                coordinates.latitude,
                coordinates.longitude), 10.0f))
          GeoJsonLayer(mMap, JSONObject(body.toJson()))
        }


        layer.features.forEach {
          it.pointStyle = GeoJsonPointStyle().apply {
            icon = if (it.getProperty("haveChronics") == "true") chronicHomeIcon else homeIcon
            title = "บ้านเลขที่ ${it.getProperty("no")}"
            snippet = """${it.getProperty("coordinates")}""".trimMargin()
          }
        }
        layer.addLayerToMap()
        layer.setOnFeatureClickListener {
          startActivity(intentFor<HouseActivity>("houseId" to it.getProperty("id")))
        }
      }
      t?.let { toast("${t.message}") }
    }


  }

  private val homeIcon by lazy { fromBitmap(drawable(R.drawable.ic_home_black_24px).toBitmap()) }
  private val chronicHomeIcon by lazy { fromBitmap(drawable(R.drawable.ic_home_red_24px).toBitmap()) }
}
