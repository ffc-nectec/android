package ffc.app.healthservice

import android.util.Log
import ffc.android.tag
import ffc.api.FfcCentral
import ffc.entity.Organization
import ffc.entity.Person
import ffc.entity.gson.toJson
import ffc.entity.healthcare.HealthCareService
import retrofit2.Call
import retrofit2.dsl.enqueue
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.ConcurrentHashMap

internal interface HealthcareServices {

    fun add(services: HealthCareService, callback: (HealthCareService?, Throwable?) -> Unit)
}

private class InMemoryHealthCareServices(val org: Organization, val person: Person) : HealthcareServices {

    val api = FfcCentral().service<HealthCareServiceApi>()

    override fun add(services: HealthCareService, callback: (HealthCareService?, Throwable?) -> Unit) {
        require(services.patientId == person.id) { "Not match patinet id" }
        Log.d(tag, "homevisit = ${services.toJson()}")
        val list = repository[person.id] ?: mutableListOf()
        list.add(services)
        repository[person.id] = list
        callback(services, null)
    }

    companion object {
        val repository: ConcurrentHashMap<String, MutableList<HealthCareService>> = ConcurrentHashMap()
    }
}

private class ApiHealthCareServices(val org: Organization) : HealthcareServices {

    val api = FfcCentral().service<HealthCareServiceApi>()

    override fun add(services: HealthCareService, callback: (HealthCareService?, Throwable?) -> Unit) {
        api.post(services, org.id).enqueue {
            onSuccess {
                callback(body()!!, null)
            }
            onError {
                callback(null, IllegalArgumentException())
            }
            onFailure {
                callback(null, it)
            }
        }
    }
}

interface HealthCareServiceApi {

    @POST("org/{orgId}/healthcareservice")
    fun post(@Body services: HealthCareService, @Path("orgId") orgId: String): Call<HealthCareService>
}

internal fun Person.healthCareServices(org: Organization): HealthcareServices = InMemoryHealthCareServices(org, this)
