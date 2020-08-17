package ffc.app.location

import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.util.RepoCallback
import ffc.entity.Organization
import ffc.entity.place.House
import me.piruin.geok.geometry.FeatureCollection
import retrofit2.dsl.enqueue

interface PlaceGeoJson {

    fun all(callbackDsl: RepoCallback<FeatureCollectionFilter<House>>.() -> Unit)
    fun noLocation(callbackDsl: RepoCallback<List<House>>.() -> Unit)
}

fun placeGeoJson(org: Organization): PlaceGeoJson = ApiPlaceGeoJson(org)

private class ApiPlaceGeoJson(val org: Organization) : PlaceGeoJson {

    val api = FfcCentral().service<PlaceService>()

    override fun all(callbackDsl: RepoCallback<FeatureCollectionFilter<House>>.() -> Unit) {
        val callback = RepoCallback<FeatureCollectionFilter<House>>().apply(callbackDsl)

        api.listHouseGeoJson(org.id).enqueue {
            onSuccess { callback.onFound!!.invoke(body()!!) }
            onError { callback.onFail!!.invoke(ApiErrorException(this)) }
            onFailure { callback.onFail!!.invoke(it) }
        }
    }
    // Call<List<House>>
    override fun noLocation(callbackDsl: RepoCallback<List<House>>.() -> Unit) {
        val callback = RepoCallback<List<House>>().apply(callbackDsl)

        api.listHouseNoLocation(org.id).enqueue {
            onSuccess { callback.onFound!!.invoke(body()!!) }
            onError { callback.onFail!!.invoke(ApiErrorException(this)) }
            onFailure { callback.onFail!!.invoke(it) }
        }
    }
}
