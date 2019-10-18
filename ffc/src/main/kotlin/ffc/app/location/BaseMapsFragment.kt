/*
 * Copyright (c) 2019 NECTEC
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

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.sembozdemir.permissionskt.handlePermissionsResult
import ffc.android.gone
import ffc.app.familyFolderActivity
import th.or.nectec.marlo.MarloFragment

open class BaseMapsFragment : MarloFragment() {

    private val org by lazy { familyFolderActivity.org }
    internal val preference by lazy { GeoPreferences(context!!, org) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preference.lastCameraPosition?.let { setStartLocation(it) }
        viewFinder.gone()
        hideToolsMenu()
    }

    private fun setStartLocation(lastPosition: CameraPosition?) {
        if (lastPosition != null) {
            setStartLocation(lastPosition.target, lastPosition.zoom)
        } else {
            setStartLocation(LatLng(13.76498, 100.538335), 5.0f)
            setStartAtCurrentLocation(true)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        activity!!.handlePermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPause() {
        super.onPause()
        googleMap?.cameraPosition?.let { preference.lastCameraPosition = it }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        super.onMapReady(googleMap)
        askMyLocationPermission()
    }

    override fun showToolsMenu() {
        //do nothing
    }

    override fun undo() = true

    override fun hideToolsMenu() {
        //do nothing
    }

    override fun mark(markPoint: LatLng?) {
        //do nothing
    }
}
