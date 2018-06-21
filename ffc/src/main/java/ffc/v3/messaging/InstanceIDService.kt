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

package ffc.v3.messaging

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import ffc.v3.api.FfcCentral
import ffc.v3.util.firebaseToken
import ffc.v3.util.org
import okhttp3.ResponseBody
import org.jetbrains.anko.defaultSharedPreferences
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

class InstanceIDService : FirebaseInstanceIdService() {

  override fun onTokenRefresh() {
    super.onTokenRefresh()
    val refreshedToken = FirebaseInstanceId.getInstance().token
    Log.d("FBIdService", "Refreshed token: " + refreshedToken!!)

    val lastToken = defaultSharedPreferences.firebaseToken
    if (lastToken != null) {
      unregisterToServer(lastToken)
    }

    sendRegistrationToServer(refreshedToken)
    defaultSharedPreferences.firebaseToken = refreshedToken
  }

  private fun unregisterToServer(firebaseToken: String) {
    FfcCentral().service<TokenService>()
      .removeToken(defaultSharedPreferences.org!!.id.toLong(), firebaseToken)
  }

  private fun sendRegistrationToServer(refreshedToken: String) {
    FfcCentral().service<TokenService>()
      .updateToken(defaultSharedPreferences.org!!.id.toLong(), Token(refreshedToken))

  }

  interface TokenService {

    @POST("org/{orgId}/mobileFirebaseToken")
    fun updateToken(@Path("orgId") orgId: Long, token: Token)
      : Call<ResponseBody>

    @DELETE("org/{orgId}/mobileFirebaseToken/{token}")
    fun removeToken(@Path("orgId") orgId: Long, @Path("token") token: String)
      : Call<ResponseBody>
  }

  class Token(val firebaseToken: String)
}
