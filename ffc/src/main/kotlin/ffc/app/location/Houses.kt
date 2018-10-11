package ffc.app.location

import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.isDev
import ffc.app.person.mockPerson
import ffc.app.util.RepoCallback
import ffc.entity.House
import ffc.entity.Organization
import ffc.entity.Person
import me.piruin.geok.geometry.Point
import retrofit2.dsl.enqueue

interface Houses {

    fun house(id: String, callbackDsl: RepoCallback<House>.() -> Unit)
}

fun housesOf(org: Organization): Houses = if (isDev) DummyHouses() else ApiHouses(org)

private class ApiHouses(val org: Organization) : Houses {

    val api = FfcCentral().service<PlaceService>()

    override fun house(id: String, callbackDsl: RepoCallback<House>.() -> Unit) {
        val callback = RepoCallback<House>().apply(callbackDsl)
        api.get(org.id, id).enqueue {
            onSuccess {
                callback.onFound!!.invoke(body()!!)
            }
            onError {
                callback.onFail!!.invoke(ApiErrorException(this))
            }
            onFailure {
                callback.onFail!!.invoke(it)
            }
        }
    }
}

private class DummyHouses() : Houses {
    override fun house(id: String, callbackDsl: RepoCallback<House>.() -> Unit) {
        val callback = RepoCallback<House>().apply(callbackDsl)
        callback.onFound!!.invoke(House().apply {
            no = "112 อุทธยานวิทยาศาสตร์"
            location = Point(13.0, 102.0)
        })
    }
}

internal fun House.resident(orgId: String, callbackDsl: RepoCallback<List<Person>>.() -> Unit) {
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
