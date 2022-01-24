/*
 * Copyright (c) 2018 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ffc.app.location

import ffc.entity.Place
import ffc.entity.place.House
import me.piruin.geok.geometry.FeatureCollection
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlaceService {

    @Headers("accept: application/vnd.geo+json")
    @GET("org/{orgId}/house")
    fun listHouseGeoJson(@Path("orgId") orgId: String): Call<FeatureCollectionFilter<House>>

    @GET("org/{orgId}/house?haveLocation=false")
    fun listHouseNoLocation(@Path("orgId") orgId: String): Call<List<House>>

    @GET("org/{orgId}/house/{houseId}")
    fun get(
        @Path("orgId") orgId: String,
        @Path("houseId") houseId: String
    ): Call<House>

    @PUT("org/{orgId}/house/{houseId}")
    fun updateHouse(
        @Path("orgId") orgId: String,
        @Body house: House,
        @Path("houseId") houseId: String = house.id
    ): Call<House>
}
