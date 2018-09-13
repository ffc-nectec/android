package ffc.app.messaging

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

internal interface TokenService {
    @POST("org/{orgId}/mobileFirebaseToken")
    fun updateToken(
        @Path("orgId") orgId: String,
        @Body token: Map<String, Any>
    ): Call<Unit>

    @DELETE("org/{orgId}/mobileFirebaseToken/{token}")
    fun removeToken(
        @Path("orgId") orgId: String,
        @Path("token") token: String
    ): Call<Unit>
}
