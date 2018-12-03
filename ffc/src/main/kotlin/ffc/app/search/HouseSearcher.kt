package ffc.app.search

import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.util.RepoCallback
import ffc.entity.place.House
import retrofit2.Call
import retrofit2.dsl.enqueue
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HouseSearcher {

    fun search(query: String, dsl: RepoCallback<List<House>>.() -> Unit)
}

fun houseSearcher(orgId: String): HouseSearcher =
    ApiHouseSearcher(orgId)

class ApiHouseSearcher(val orgId: String) : HouseSearcher {

    val api = FfcCentral().service<HouseSearchApi>()

    override fun search(query: String, dsl: RepoCallback<List<House>>.() -> Unit) {
        val callback = RepoCallback<List<House>>().apply(dsl)
        api.getQuery(orgId, query).enqueue {
            always { callback.always?.invoke() }
            onSuccess {
                val list = body()!!
                if (list.isNotEmpty()) {
                    callback.onFound!!.invoke(list)
                } else {
                    callback.onNotFound!!.invoke()
                }
            }
            onError { callback.onFail!!.invoke(ApiErrorException(this)) }
            onFailure { callback.onFail!!.invoke(it) }
        }
    }
}

interface HouseSearchApi {

    @GET("org/{orgId}/house")
    fun getQuery(
        @Path("orgId") orgId: String,
        @Query("query") query: String
    ): Call<List<House>>
}
