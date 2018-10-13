package ffc.app.healthservice

import android.util.Log
import ffc.android.tag
import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.isDev
import ffc.app.util.RepoCallback
import ffc.entity.gson.toJson
import ffc.entity.healthcare.HealthCareService
import retrofit2.dsl.enqueue
import java.util.concurrent.ConcurrentHashMap

internal interface HealthCareServices {

    fun add(services: HealthCareService, callback: (HealthCareService?, Throwable?) -> Unit)

    fun all(dsl: RepoCallback<List<HealthCareService>>.() -> Unit)
}

private class InMemoryHealthCareServices(val personId: String) : HealthCareServices {

    override fun add(services: HealthCareService, callback: (HealthCareService?, Throwable?) -> Unit) {
        require(services.patientId == personId) { "Not match patinet id" }
        Log.d(tag, "homevisit = ${services.toJson()}")
        val list = repository[personId] ?: mutableListOf()
        list.add(services)
        repository[personId] = list
        callback(services, null)
    }

    override fun all(dsl: RepoCallback<List<HealthCareService>>.() -> Unit) {
        val callback = RepoCallback<List<HealthCareService>>().apply(dsl)
        callback.always?.invoke()
        val list = repository[personId] ?: listOf<HealthCareService>()
        if (list.isNotEmpty()) {
            callback.onFound!!.invoke(list)
        } else {
            callback.onNotFound!!.invoke()
        }
    }

    companion object {
        val repository: ConcurrentHashMap<String, MutableList<HealthCareService>> = ConcurrentHashMap()
    }
}

private class ApiHealthCareServices(
    val org: String,
    val person: String
) : HealthCareServices {

    val api = FfcCentral().service<HealthCareServiceApi>()

    override fun add(services: HealthCareService, callback: (HealthCareService?, Throwable?) -> Unit) {
        api.post(services, org).enqueue {
            onSuccess {
                callback(body()!!, null)
            }
            onError {
                callback(null, ApiErrorException(this))
            }
            onFailure {
                callback(null, it)
            }
        }
    }

    override fun all(dsl: RepoCallback<List<HealthCareService>>.() -> Unit) {
        val callback = RepoCallback<List<HealthCareService>>().apply(dsl)
        api.get(org, person).enqueue {
            onSuccess {
                callback.onFound!!.invoke(body()!!)
            }
            onClientError {
                callback.onNotFound!!.invoke()
            }
            onServerError {
                callback.onFail!!.invoke(ApiErrorException(this))
            }
            onFailure {
                callback.onFail!!.invoke(it)
            }
        }
    }
}

internal fun healthCareServicesOf(personId: String, orgId: String): HealthCareServices = if (isDev)
    InMemoryHealthCareServices(personId) else ApiHealthCareServices(orgId, personId)