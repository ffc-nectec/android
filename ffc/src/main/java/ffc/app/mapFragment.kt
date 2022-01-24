package ffc.app

import android.Manifest
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
//import android.support.v4.app.ActivityCompat
//import android.support.v4.app.Fragment
//import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import ffc.android.onClick
import kotlinx.android.synthetic.main.fragment_map.*
import java.io.IOException
import java.util.*


class mapFragment : Fragment(),  OnMapReadyCallback, LocationListener {

    fun mapFragment() {}
    private var mMap: GoogleMap? = null
    private var locationManager: LocationManager? = null
    private val MIN_TIME: Long = 400
    private val MIN_DISTANCE = 1000
    private val REQUEST_LOCATION = 1
    private var resultLatLng: LatLng? = null
    private var geocoder: Geocoder? = null
    private var locationString: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_map, container, false)
        geocoder = Geocoder(context, Locale("th", "TH"))
        (childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?)!!.getMapAsync { googleMap ->
            mMap = googleMap
//            mMap!!.isMyLocationEnabled = true
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //buildGoogleApiClient()
                    mMap!!.isMyLocationEnabled = true
                }
            } else {
                //buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
            mMap!!.setOnCameraIdleListener {
                val cameraPosition: CameraPosition = mMap!!.getCameraPosition()
                if (cameraPosition != null) {
                    try {
                        updateAddress(cameraPosition.target)
                        resultLatLng = LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        //Alert.showAlertOK(activity, "ไม่สามารถเชื่อมต่อกับบริการของ Google ได้ กรุณารีสตาร์ทเครื่องใหม่และลองอีกครั้ง", true, {})
                    }
                    Log.e("Location Changed", cameraPosition.target.toString())
                }
                shareLayout.onClick {
                    onShareLayout()
                }
            }
            getLocation();
        }

        return view;
    }
    fun onShareLayout() {
        if (resultLatLng == null) {
            //Alert.showAlertOK(activity, "กรุณาเลือกพิกัด", false, null)
            return
        }
        val resultIntent = Intent()
        resultIntent.putExtra("lat", resultLatLng!!.latitude)
        resultIntent.putExtra("lon", resultLatLng!!.longitude)
        resultIntent.putExtra("location", locationString)
        requireActivity().setResult(Activity.RESULT_OK, resultIntent)
        requireActivity().finish()
    }
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Activity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {
            locationManager = this.requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager?
            val locationGPS = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (locationGPS != null) {
                val lat = locationGPS.latitude
                val longi = locationGPS.longitude
                var latitude = lat
                var longitude = longi
                val latLng = LatLng(latitude, longitude)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                mMap!!.animateCamera(cameraUpdate)
//                mMap!!.addMarker(MarkerOptions()
//                    .position(latLng)
//                    .title("Hello world")
//                    )
            } else {
                Toast.makeText(context, "Unable to find location.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onLocationChanged(p0: Location?) {
        val latLng = LatLng(p0!!.getLatitude(), p0!!.getLongitude())
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
        mMap!!.animateCamera(cameraUpdate)
    }

    override fun onMapReady(p0: GoogleMap?) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            //buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }
    }

//    override fun onCameraIdle() {
//        val cameraPosition: CameraPosition = mMap!!.getCameraPosition()
//        if (cameraPosition != null) {
//            try {
//                updateAddress(cameraPosition.target)
//                resultLatLng = LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude)
//            } catch (e: IOException) {
//                e.printStackTrace()
//                //Alert.showAlertOK(activity, "ไม่สามารถเชื่อมต่อกับบริการของ Google ได้ กรุณารีสตาร์ทเครื่องใหม่และลองอีกครั้ง", true, {})
//            }
//            Log.e("Location Changed", cameraPosition.target.toString())
//        }
//    }
    @Throws(IOException::class)
    private fun updateAddress(latLng: LatLng) {
        var addressString = ""
        val addresses: List<Address>
        addresses = geocoder!!.getFromLocation(latLng.latitude, latLng.longitude, 1)
        var address: String? = null
        var city: String? = null
        var state: String? = null
        var country: String? = null
        var postalCode: String? = null
        var knownName: String? = null
        if (addresses != null) {
            if (addresses.size > 0) {
                address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses[0].locality
                state = addresses[0].adminArea
                country = addresses[0].countryName
                postalCode = addresses[0].postalCode
                knownName = addresses[0].featureName
            }
        }
//        if (address != null) {
//            addressString = addressString + address
//        }
//        if (city != null) {
//            if (addressString != "") {
//                addressString = "$addressString, "
//            }
//            addressString = addressString + city
//        }
//        if (state != null) {
//            if (addressString != "") {
//                addressString = "$addressString, "
//            }
//            addressString = addressString + state
//        }
//        if (postalCode != null) {
//            if (addressString != "") {
//                addressString = "$addressString "
//            }
//            addressString = addressString + postalCode
//        }
        locationString = address
        locationTextView.setText(address)
    }

}
