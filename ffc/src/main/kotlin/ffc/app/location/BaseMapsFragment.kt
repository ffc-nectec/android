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
        setStartLocation(preference.lastCameraPosition!!)
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
