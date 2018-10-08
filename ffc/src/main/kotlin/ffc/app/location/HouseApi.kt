package ffc.app.location

import ffc.entity.Person
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HouseApi {

    @GET("org/{orgId}/place/house/{houseId}/resident")
    fun personInHouse(
        @Path("orgId") orgId: String,
        @Path("houseId") houseId: String
    ): Call<List<Person>>
}
