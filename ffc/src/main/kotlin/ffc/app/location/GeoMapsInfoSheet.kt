package ffc.app.location

import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.view.View.OnClickListener
import ffc.android.onClick
import ffc.app.MainActivity
import ffc.app.R
import ffc.app.location.GeoMapsInfo.Chronic
import ffc.app.location.GeoMapsInfo.ELDER
import ffc.app.location.GeoMapsInfo.NORMAL
import org.jetbrains.anko.find

class GeoMapsInfoSheet(actvity: MainActivity, geoMapsFragment: GeoMapsFragment) {

    val fab = actvity.find<FloatingActionButton>(R.id.addLocationButton)
    private val behavior = BottomSheetBehavior.from(actvity.find<View>(R.id.bottom_sheet))!!

    var currentInfo: GeoMapsInfo = NORMAL

    val onClick = OnClickListener {
        val newInfo = when (it.id) {
            R.id.elderMapInfo -> ELDER
            R.id.cvdMapInfo -> Chronic
            else -> NORMAL
        }
        if (newInfo != currentInfo) {
            geoMapsFragment.showInfo(newInfo) {
                //TODO implement progress bar
            }
            currentInfo = newInfo
        }
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    init {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(view: View, slideOffset: Float) {
            }

            override fun onStateChanged(view: View, newState: Int) {
                if (BottomSheetBehavior.STATE_SETTLING == newState) {
                    fab.animate().scaleX(0f).scaleY(0f).setDuration(300).start()
                } else if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    fab.animate().scaleX(1f).scaleY(1f).setDuration(300).start()
                }
            }
        })
        actvity.find<View>(R.id.elderMapInfo).setOnClickListener(onClick)
        actvity.find<View>(R.id.cvdMapInfo).setOnClickListener(onClick)

        fab.onClick {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}

enum class GeoMapsInfo {
    NORMAL, ELDER, Chronic, Disability
}
