package ffc.app.location

import android.util.Log
import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.mockRepository
import ffc.app.person.mockPerson
import ffc.app.util.RepoCallback
import ffc.app.util.TaskCallback
import ffc.entity.Organization
import ffc.entity.Person
import ffc.entity.place.House
import me.piruin.geok.geometry.Point
import retrofit2.dsl.enqueue

interface Houses {

    fun house(id: String, callbackDsl: RepoCallback<House>.() -> Unit)

    fun houseNoLocation(callbackDsl: RepoCallback<List<House>>.() -> Unit)
}

interface HouseManipulator {

    fun update(callbackDsl: TaskCallback<House>.() -> Unit)
}

fun housesOf(org: Organization): Houses = if (mockRepository) DummyHouses() else ApiHouses(org)

private class ApiHouses(val org: Organization) : Houses {

    val api = FfcCentral().service<PlaceService>()

    override fun house(id: String, callbackDsl: RepoCallback<House>.() -> Unit) {
        val callback = RepoCallback<House>().apply(callbackDsl)
        api.get(org.id, id).enqueue {
            onSuccess {
//                Log.d("==> body <==",body().toString())
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

    override fun houseNoLocation(callbackDsl: RepoCallback<List<House>>.() -> Unit) {
        val callback = RepoCallback<List<House>>().apply(callbackDsl)
        api.listHouseNoLocation(org.id).enqueue {
            onSuccess {
                callback.onFound!!.invoke(body()!!)
            }
            onError {
                if (code() == 404)
                    callback.onNotFound!!.invoke()
                else
                    callback.onFail!!.invoke(ApiErrorException(this))
            }
            onFailure {
                callback.onFail!!.invoke(it)
            }
        }
    }
}

private class ApiHouseManipulator(val org: Organization, val house: House) : HouseManipulator {

    val api = FfcCentral().service<PlaceService>()

    override fun update(callbackDsl: TaskCallback<House>.() -> Unit) {
        val callback = TaskCallback<House>().apply(callbackDsl)
        api.updateHouse(org.id, house).enqueue {
            onSuccess {
                callback.result(body()!!)
            }
            onError {
                callback.expception!!.invoke(ApiErrorException(this))
            }
            onFailure {
                callback.expception!!.invoke(it)
            }
        }
    }
}

private class DummyHouses(val house: House? = null) : Houses, HouseManipulator {

    override fun houseNoLocation(callbackDsl: RepoCallback<List<House>>.() -> Unit) {
        val callback = RepoCallback<List<House>>().apply(callbackDsl)
        val houses = mutableListOf<House>()
        for (i in 1..100) {
            houses.add(House().apply { no = "100/$i" })
        }
        callback.onFound!!.invoke(houses)
    }

    override fun update(callbackDsl: TaskCallback<House>.() -> Unit) {
        val callback = TaskCallback<House>().apply(callbackDsl)
        callback.result(house!!)
    }

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

    if (mockRepository) {
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

fun House.manipulator(org: Organization): HouseManipulator {
    return if (mockRepository) DummyHouses(this) else ApiHouseManipulator(org, this)
}

internal fun House.pushTo(org: Organization, callbackDsl: TaskCallback<House>.() -> Unit) {
    manipulator(org).update(callbackDsl)
}
