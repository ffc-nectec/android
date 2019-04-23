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

package ffc.app.auth

import ffc.entity.Token
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

    @POST("org/{orgId}/authorize")
    fun createAuthorize(
        @Path("orgId") orgId: String,
        @Body body: LoginBody
    ): Call<Token>

    @POST("org/{orgId}/activate/user")
    fun activateUser(
        @Path("orgId") orgId: String,
        @Body body: ActivateBody
    ): Call<Token>
}

class LoginBody(val username: String, val password: String)

class ActivateBody(val otp: String, val username: String, val password: String) {
    constructor(otp: String, login: LoginBody) : this(otp, login.username, login.password)
}
