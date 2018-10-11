package ffc.app.location

import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.isDev
import ffc.app.person.mockPerson
import ffc.app.util.RepoCallback
import ffc.entity.House
import ffc.entity.Person
import retrofit2.dsl.enqueue

fun House.resident(orgId: String, callbackDsl: RepoCallback<List<Person>>.() -> Unit) {
    val callback = RepoCallback<List<Person>>().apply(callbackDsl)

    if (isDev) {
        callback.onFound!!.invoke(listOf(mockPerson))
    } else {
        FfcCentral().service<HouseApi>().personInHouse(orgId, this.id).enqueue {
            onSuccess { callback.onFound!!.invoke(body()!!) }
            onClientError { callback.onNotFound!!.invoke() }
            onServerError { callback.onFail!!.invoke(ApiErrorException(this)) }
            onFailure { callback.onFail!!.invoke(it) }
        }
    }
}
