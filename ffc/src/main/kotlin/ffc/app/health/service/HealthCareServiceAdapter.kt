package ffc.app.health.service

//import android.support.annotation.DrawableRes
//import android.support.annotation.StringRes
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felipecsl.asymmetricgridview.AsymmetricRecyclerView

import ffc.android.drawable
import ffc.android.getString
import ffc.android.gone
import ffc.android.onClick
import ffc.android.visible
import ffc.app.R
import ffc.app.photo.asymmetric.bind
import ffc.app.util.AdapterClickListener
import ffc.app.util.datetime.toBuddistString
import ffc.app.util.takeIfNotEmpty
import ffc.app.util.takeIfNotNullOrBlank
import ffc.app.util.timeago.toTimeAgo
import ffc.app.util.value.Value
import ffc.app.util.value.ValueAdapter
import ffc.entity.Lang
import ffc.entity.Lookup
import ffc.entity.healthcare.CommunityService
import ffc.entity.healthcare.Diagnosis
import ffc.entity.healthcare.HealthCareService
import ffc.entity.healthcare.HomeVisit
import ffc.entity.healthcare.SpecialPP
import org.jetbrains.anko.find
import org.joda.time.LocalDate

class HealthCareServiceAdapter(
    val services: List<HealthCareService>,
    val limit: Int = 10,
    onClickDsl: AdapterClickListener<HealthCareService>.() -> Unit
) : RecyclerView.Adapter<HealthCareServiceViewHolder>() {

    private val listener = AdapterClickListener<HealthCareService>().apply(onClickDsl)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): HealthCareServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hs_service_item, parent, false)
        return HealthCareServiceViewHolder(view)
    }

    override fun getItemCount() = if (services.size > limit) limit else services.size

    override fun onBindViewHolder(holder: HealthCareServiceViewHolder, position: Int) {
        holder.bind(services[position])
        holder.itemView.onClick {
            listener.onItemClick!!.invoke(holder.itemView, services[position])
        }
        listener.bindOnViewClick(holder.itemView, services[position], holder.editButton)
    }
}

class HealthCareServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val icon = view.find<ImageView>(R.id.serviceIconView)
    private val title = view.find<TextView>(R.id.serviceTitleView)
    private val date = view.find<TextView>(R.id.serviceDateView)
    private val detailView = view.find<RecyclerView>(R.id.detailView)
    private val photos = view.find<AsymmetricRecyclerView>(R.id.photos)
    val editButton = view.find<ImageButton>(R.id.editButton)

    init {
        detailView.layoutManager = LinearLayoutManager(itemView.context)
    }

    private val HealthCareService.isEditable: Boolean
        get() = time.toLocalDate().isEqual(LocalDate.now())

    fun bind(services: HealthCareService) {
        with(services) {
            date.text = services.time.toTimeAgo()
            val type = typeOf(services)
            title.text = itemView.context.getString(type.titleRes)
            icon.setImageDrawable(itemView.context.drawable(type.iconRes))
            photos.bind(photosUrl)
            detailView.adapter = ValueAdapter(toValue(), ValueAdapter.Style.SMALL, true)
            if (type == ServiceType.HOME_VISIT && isEditable) {
                editButton.visible()
            } else {
                editButton.gone()
            }
        }
    }

    private fun typeOf(services: HealthCareService): ServiceType {
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

private fun HealthCareService.toValue(): List<Value> {
    val values = mutableListOf<Value>()
    syntom.takeIfNotNullOrBlank()?.let { values.add(Value("อาการเบื้องต้น", it)) }
    nextAppoint?.let { values.add(Value("นัดครั้งต่อไป", it.toBuddistString())) }
    diagnosises.toValue().takeIfNotEmpty()?.let { values.addAll(it) }
    specialPPs.toSpecialPpValues().takeIfNotEmpty()?.let { values.addAll(it) }
    communityServices.toCommunityServiceValues().takeIfNotEmpty()?.let { values.addAll(it) }
    suggestion.takeIfNotNullOrBlank()?.let { values.add(Value("คำแนะนำ", it)) }
    note.takeIfNotNullOrBlank()?.let { values.add(Value("บันทึก", it)) }
    return values
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
    get() = translation[Lang.th].takeIfNotNullOrBlank() ?: name
