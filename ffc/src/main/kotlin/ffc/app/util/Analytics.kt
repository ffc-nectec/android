package ffc.app.util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import ffc.entity.Entity
import ffc.entity.Organization
import ffc.entity.Person
import ffc.entity.User
import ffc.entity.gson.toJson
import ffc.entity.healthcare.HealthCareService
import ffc.entity.healthcare.HomeVisit
import ffc.entity.healthcare.bloodPressureLevel
import ffc.entity.place.House
import org.jetbrains.anko.bundleOf
import org.joda.time.Days
import org.joda.time.LocalDate

interface Analytics {

    var user: User?
    var org: Organization?

    fun view(entity: Entity)

    fun search(term: String)

    fun service(service: HealthCareService, person: Person?)

    companion object {

        var instance: Analytics? = null

        fun init(context: Context) {
            instance = FBAnalytics(context)
        }

        fun setUserProperty(org: Organization?, user: User?) {
            instance?.user = user
            instance?.org = org
        }

        fun close() {
            instance = null
        }
    }
}

private class FBAnalytics(context: Context) : Analytics {

    var _org: Organization? = null
    override var org: Organization?
        get() = _org
        set(value) {
            _org = value
            analytics.setUserProperty("org_name", _org?.name)
            analytics.setUserProperty("org_location", _org?.location?.toJson())
        }

    var _user: User? = null
    override var user: User?
        get() = _user
        set(value) {
            _user = value
            analytics.setUserId(_user?.id?.md5())
        }

    val analytics = FirebaseAnalytics.getInstance(context)

    override fun view(entity: Entity) {
        val bundle = entity.toAnalyticsBundle()
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)

        //figuring out, Which style of event name is better?
        analytics.logEvent(when (entity) {
            is Person -> "view_person"
            is House -> "view_house"
            else -> "view_entity"
        }, bundle)
    }

    override fun search(term: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundleOf(
            FirebaseAnalytics.Param.SEARCH_TERM to term
        ))
    }

    override fun service(service: HealthCareService, person: Person?) {
        val bundle = person?.toAnalyticsBundle() ?: Bundle()
        bundle.putAll(service.toAnalyticsBundle())

        analytics.logEvent("healthcare_service", bundle)
    }

    private fun Entity.toAnalyticsBundle(): Bundle {
        val bundle = bundleOf(
            FirebaseAnalytics.Param.ITEM_ID to id,
            FirebaseAnalytics.Param.CONTENT_TYPE to type
        )
        when (this) {
            is Person -> {
                bundle.putAll(
                    FirebaseAnalytics.Param.ITEM_NAME to name,
                    "age" to age,
                    "chronic_count" to chronics.filter { it.isActive }.size,
                    "gender" to sex,
                    "alive" to !isDead,
                    "avartar_present" to (avatarUrl != null),
                    "health_issue" to healthAnalyze?.result?.filterValues { it.haveIssue }?.size
                )
            }
            is House -> {
                bundle.putAll(
                    "location" to location?.toJson(),
                    "no" to no,
                    "village_id" to villageId,
                    "village_name" to villageName,
                    "have_chronic" to haveChronic,
                    "resident_count" to people.size,
                    "image_count" to imagesUrl.size
                )
            }
            is HealthCareService -> {
                bundle.putAll(
                    "patient_id" to patientId,
                    "provider_id" to providerId.md5(),
                    "time" to time.toString(),
                    "principle_dx" to principleDx?.id,
                    "principle_dx_name" to principleDx?.name,
                    "diagnosis_count" to diagnosises.size,
                    "community_service_count" to communityServices.size,
                    "image_count" to photosUrl.size,
                    "bmi" to bmi?.value,
                    "bp_sys" to bloodPressure?.systolic,
                    "bp_dia" to bloodPressure?.diastolic,
                    "appoint_day" to Days.daysBetween(LocalDate.now(), nextAppoint
                        ?: LocalDate.now()).getDays(),
                    "bp_level" to when {
                        bloodPressureLevel?.isHigh == true -> "high"
                        bloodPressureLevel?.isPreHigh == true -> "pre_high"
                        bloodPressureLevel?.isNormal == true -> "normal"
                        bloodPressureLevel?.isLow == true -> "low"
                        else -> null
                    }
                )
                communityServices.firstOrNull { it is HomeVisit }?.let {
                    it as HomeVisit
                    bundle.putAll(
                        "homevisit_type_id" to it.serviceType.id,
                        "homevisit_type_name" to it.serviceType.name,
                        "homevisit_detail_length" to it.detail?.length,
                        "homevisit_result_length" to it.result?.length,
                        "homevisit_plan_length" to it.plan?.length
                    )
                }
            }
        }
        return bundle
    }

    private fun Bundle.putAll(vararg param: Pair<String, Any?>) {
        putAll(bundleOf(*param))
    }
}
