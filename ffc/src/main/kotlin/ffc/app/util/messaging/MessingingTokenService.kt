/*
 * Copyright (c) 2019 NECTEC
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

package ffc.app.util.messaging

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

internal interface MessingingTokenService {
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
