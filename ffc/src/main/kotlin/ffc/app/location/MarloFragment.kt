package ffc.app.location

import android.Manifest
import android.annotation.SuppressLint
import com.sembozdemir.permissionskt.askPermissions
import th.or.nectec.marlo.MarloFragment

@SuppressLint("MissingPermission")
internal fun MarloFragment.askMyLocationPermission() {
    activity!!.askPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) {
        onGranted {
            enableMyLocationButton()
        }
    }
}
