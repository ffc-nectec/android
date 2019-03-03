package ffc.app.location

import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import ffc.android.drawable
import ffc.android.layoutInflater
import ffc.android.onClick
import ffc.app.MainActivity
import ffc.app.R
import ffc.app.location.GeoMapsInfo.Chronic
import ffc.app.location.GeoMapsInfo.Disability
import ffc.app.location.GeoMapsInfo.ELDER
import ffc.app.location.GeoMapsInfo.NORMAL
import org.jetbrains.anko.find

class GeoMapsInfoSheet(actvity: MainActivity, geoMapsFragment: GeoMapsFragment) {

    private val fab = actvity.find<FloatingActionButton>(R.id.addLocationButton)
    private val progress = actvity.find<ProgressBar>(R.id.progress)
    val peek = actvity.find<ViewGroup>(R.id.mapInfoPeek)
    private val behavior = BottomSheetBehavior.from(actvity.find<View>(R.id.bottom_sheet))!!

    var currentInfo: GeoMapsInfo = Chronic

    val onClick = OnClickListener { view ->
        val newInfo = when (view.id) {
            R.id.elderMapInfo -> ELDER
            R.id.cvdMapInfo -> Chronic
            R.id.disabilityMapInfo -> Disability
            else -> NORMAL
        }
        if (newInfo != currentInfo) {
            currentInfo = newInfo
            progress.progress = 0
            geoMapsFragment.showInfo(newInfo) {
                progress.progress = (it * 100.0).toInt()
                if (it == 1.0) {
                    progress.animate().scaleX(0f).setDuration(150).start()
                    peek.removeAllViews()
                    peek.addView(newInfo.getLegendView())
                }
            }
            progress.animate().scaleX(1f).setDuration(50).setStartDelay(300).start()
            peek.removeAllViews()
            peek.addView(loadingPeek)
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
        actvity.find<View>(R.id.disabilityMapInfo).setOnClickListener(onClick)

        progress.max = 100
        progress.animate().scaleX(0f).setDuration(0).start()

        fab.onClick {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        peek.addView(currentInfo.getLegendView())
    }

    fun GeoMapsInfo.getLegendView(): View {
        val view = peek.layoutInflater.inflate(R.layout.maps_info_legend_peek, peek, false)
        val legend = view.find<LegendView>(R.id.mapInfoLegend)
        val icon = view.find<ImageView>(R.id.mapsInfoIcon)
        icon.onClick { behavior.state = BottomSheetBehavior.STATE_EXPANDED }
        when (this) {
            ELDER -> {
                legend.setLegend(
                    R.color.colorAccentLegacy to R.string.elder_socialist,
                    R.color.yellow_500 to R.string.elder_home,
                    R.color.red_500 to R.string.elder_immobilised
                )
                icon.setImageDrawable(view.drawable(R.drawable.ic_elder_couple_color_24dp))
            }
            Chronic -> {
                legend.setLegend(
                    R.color.colorAccentLegacy to R.string.normal,
                    R.color.red_500 to R.string.chronic
                )
                icon.setImageDrawable(view.drawable(R.drawable.ic_heart_cvd_color_24dp))
            }
            Disability -> {
                legend.setLegend(
                    R.color.grey_300 to R.string.normal,
                    R.color.purple_300 to R.string.disability
                )
                icon.setImageDrawable(view.drawable(R.drawable.ic_wheelchair_color_24dp))
            }
        }
        return view
    }

    val loadingPeek: View by lazy {
        peek.layoutInflater.inflate(R.layout.maps_info_peek_loading, peek, false)
    }
}

enum class GeoMapsInfo {
    NORMAL, ELDER, Chronic, Disability
}
