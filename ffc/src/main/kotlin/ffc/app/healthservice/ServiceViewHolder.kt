package ffc.app.healthservice

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.felipecsl.asymmetricgridview.AsymmetricRecyclerView
import ffc.android.drawable
import ffc.android.getString
import ffc.app.R
import ffc.app.photo.asymmetric.bind
import ffc.app.util.timeago.toTimeAgo
import ffc.entity.Lang
import ffc.entity.Lookup
import ffc.entity.healthcare.CommunityService
import ffc.entity.healthcare.Diagnosis
import ffc.entity.healthcare.HealthCareService
import ffc.entity.healthcare.HomeVisit
import ffc.entity.healthcare.Icd10
import ffc.entity.healthcare.SpecialPP
import org.jetbrains.anko.find

class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val icon = view.find<ImageView>(R.id.serviceIconView)
    val title = view.find<TextView>(R.id.serviceTitleView)
    val date = view.find<TextView>(R.id.serviceDateView)
    val dx = view.find<TextView>(R.id.serviceDxView)
    val caption = view.find<TextView>(R.id.captionView)
    val diagnosis = view.find<RecyclerView>(R.id.diseasesView)
    val communityService = view.find<RecyclerView>(R.id.communityServiceView)
    val specialPp = view.find<RecyclerView>(R.id.specialPpView)
    val photos = view.find<AsymmetricRecyclerView>(R.id.photos)

    init {
        diagnosis.layoutManager = LinearLayoutManager(itemView.context)
        communityService.layoutManager = LinearLayoutManager(itemView.context)
        specialPp.layoutManager = LinearLayoutManager(itemView.context)
    }

    fun bind(services: HealthCareService) {
        with(services) {
            date.text = services.time.toTimeAgo()
            dx.text = (services.principleDx!! as Icd10).icd10

            val type = typeOf(services)
            title.text = getString(type.titleRes)
            icon.setImageDrawable(itemView.context.drawable(type.iconRes))
            //caption.text = (communityServices[0] as HomeVisit).serviceType.name
            caption.visibility = if (caption.text.isNullOrBlank()) View.GONE else View.VISIBLE
            photos.bind(photosUrl)


            diagnosis.adapter = HealthValueAdapter(diagnosises.toValue(), HealthValueAdapter.Style.SMALL, true)
            communityService.adapter = HealthValueAdapter(communityServices.toCommunityServiceValues(), HealthValueAdapter.Style.SMALL, true)
            specialPp.adapter = HealthValueAdapter(specialPPs.toSpecialPpValues(), HealthValueAdapter.Style.SMALL, true)
        }
    }

    fun typeOf(services: HealthCareService): ServiceType {
        return when {
            services.specialPPs.isNotEmpty() -> ServiceType.SPECIAL_PP
            services.communityServices.isNotEmpty() -> {
                if (services.communityServices.firstOrNull { it is HomeVisit } != null)
                    ServiceType.HOME_VISIT
                else
                    ServiceType.COMMUNITY_SERVICE
            }
            services.ncdScreen != null -> ServiceType.NCD_SCREENING
            else -> ServiceType.SERVICE
        }
    }

    enum class ServiceType(
        @DrawableRes val iconRes: Int = R.drawable.ic_stethoscope_color_24dp,
        @StringRes val titleRes: Int = R.string.general_service
    ) {
        SERVICE,
        NCD_SCREENING(R.drawable.ic_loupe_color_24dp, R.string.ncd_screen),
        HOME_VISIT(R.drawable.ic_home_visit_color_24dp, R.string.home_visit),
        COMMUNITY_SERVICE(R.drawable.ic_home_visit_color_24dp, R.string.community_service),
        SPECIAL_PP(R.drawable.ic_shield_color_24dp, R.string.special_pp)
    }
}

private fun List<SpecialPP>.toSpecialPpValues(): List<Value> {
    return map { Value("ส่งเสริมป้องกัน", "· ${it.ppType.nameTh}") }
}

private fun List<CommunityService>.toCommunityServiceValues(): List<Value> {
    return map { Value("บริการชุมชน", "· ${it.serviceType.nameTh}") }
}

private fun List<Diagnosis>.toValue(): List<Value> {
    return sortedBy { it.dxType }.map { Value("วินิจฉัย", "· ${it.disease.nameTh}") }
}

val Lookup.nameTh: String
    get() = translation[Lang.th] ?: name
