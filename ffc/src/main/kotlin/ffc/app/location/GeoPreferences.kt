package ffc.app.location

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.CameraPosition
import ffc.android.get
import ffc.android.put
import ffc.entity.Organization
import ffc.entity.place.House
import org.jetbrains.anko.doAsync

class GeoPreferences(context: Context, val org: Organization?) {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("geomap-${org?.id}", Context.MODE_PRIVATE)
    }

    var lastCameraPosition: CameraPosition?
        set(value) {
            doAsync { preferences.edit().put("campos", value).apply() }
        }
        get() = preferences.get("campos")

    var geojsonCache: FeatureCollectionFilter<House>?
        set(value) {
            preferences.edit().put("geojson", value).apply()
        }
        get() = preferences.get("geojson")
}
