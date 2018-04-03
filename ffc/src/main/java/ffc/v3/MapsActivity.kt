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
import ffc.v3.util.drawable
import ffc.v3.util.toBitmap
import org.jetbrains.anko.intentFor

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

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(13.0, 102.1), 10.0f))

    val layer = GeoJsonLayer(mMap, R.raw.place, applicationContext)
    layer.features.forEach {
      it.pointStyle = GeoJsonPointStyle().apply {
        icon = if (it.getProperty("haveChronics") == "true") chronicHomeIcon else homeIcon
        title = "บ้านเลขที่ ${it.getProperty("no")}"
        snippet = it.getProperty("coordinates")
      }
    }
    layer.addLayerToMap()
    layer.setOnFeatureClickListener {
      startActivity(intentFor<HouseActivity>("houseId" to it.getProperty("id")))
    }
  }

  private val homeIcon by lazy { fromBitmap(drawable(R.drawable.ic_home_black_24px).toBitmap()) }
  private val chronicHomeIcon by lazy { fromBitmap(drawable(R.drawable.ic_home_red_24px).toBitmap()) }
}
