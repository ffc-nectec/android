package ffc.app.location

import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
import android.support.design.widget.FloatingActionButton
import android.view.View
import ffc.android.onClick
import ffc.app.MainActivity
import ffc.app.R
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class GeoMapsInfoSheet(val actvity: MainActivity, geoMapsFragment: GeoMapsFragment) {

    val fab = actvity.find<FloatingActionButton>(R.id.addLocationButton)
    val behavior = BottomSheetBehavior.from(actvity.find<View>(R.id.bottom_sheet))

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
        actvity.find<View>(R.id.elderMapInfo).onClick {
            actvity.toast("elder")
            geoMapsFragment.showInfo(GeoMapsInfo.ELDER)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        actvity.find<View>(R.id.cvdMapInfo).onClick {
            actvity.toast("chronic")
            geoMapsFragment.showInfo(GeoMapsInfo.Chronic)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }


        fab.onClick {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}

enum class GeoMapsInfo {
    ELDER, Chronic, Disability
}
