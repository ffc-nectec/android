package ffc.app.health.service

import android.util.Log
import ffc.android.tag
import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.mockRepository
import ffc.app.util.RepoCallback
import ffc.app.util.TaskCallback
import ffc.entity.gson.toJson
import ffc.entity.healthcare.HealthCareService
import retrofit2.dsl.enqueue
import java.util.concurrent.ConcurrentHashMap

internal interface HealthCareServices {

    fun add(services: HealthCareService, dslCallback: TaskCallback<HealthCareService>.() -> Unit)

    fun all(dsl: RepoCallback<List<HealthCareService>>.() -> Unit)

    fun update(service: HealthCareService, dslCallback: TaskCallback<HealthCareService>.() -> Unit)
}

private class InMemoryHealthCareServices(val personId: String) : HealthCareServices {

    override fun update(service: HealthCareService, dslCallback: TaskCallback<HealthCareService>.() -> Unit) {
        val callback = TaskCallback<HealthCareService>().apply(dslCallback)
        repository[personId]?.let {
            it.remove(service)
            it.add(service)
        }
        callback.result(service)
    }

    override fun add(services: HealthCareService, dslCallback: TaskCallback<HealthCareService>.() -> Unit) {
        require(services.patientId == personId) { "Not match patinet id" }
        Log.d(tag, "homevisit = ${services.toJson()}")

        val callback = TaskCallback<HealthCareService>().apply(dslCallback)
        val list = repository[personId] ?: mutableListOf()
        list.add(services)
        repository[personId] = list
        callback.result.invoke(services)
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

    override fun update(service: HealthCareService, dslCallback: TaskCallback<HealthCareService>.() -> Unit) {
        val callback = TaskCallback<HealthCareService>().apply(dslCallback)
        api.put(org, service).enqueue {
            onSuccess { callback.result(body()!!) }
            onError { callback.expception?.invoke(ApiErrorException(this)) }
            onFailure { callback.expception?.invoke(it) }
        }
    }

    val api = FfcCentral().service<HealthCareServiceApi>()

    override fun add(services: HealthCareService, dslCallback: TaskCallback<HealthCareService>.() -> Unit) {
        val callback = TaskCallback<HealthCareService>().apply(dslCallback)

        api.post(services, org).enqueue {
            onSuccess {
                callback.result.invoke(body()!!)
            }
            onError {
                callback.expception?.invoke(ApiErrorException(this))
            }
            onFailure {
                callback.expception?.invoke(it)
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

internal fun healthCareServicesOf(personId: String, orgId: String): HealthCareServices = if (mockRepository)
    InMemoryHealthCareServices(personId) else ApiHealthCareServices(orgId, personId)
