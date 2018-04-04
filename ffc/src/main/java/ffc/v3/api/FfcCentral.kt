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

package ffc.v3.api

import ffc.v3.util.defaultGson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

class FfcCentral(url: String = "https://cef2febb-c769-4849-9a46-1e1b267a99af.mock.pstmn.io") {

  val httpBuilder: OkHttpClient.Builder =
    OkHttpClient.Builder()
      .readTimeout(60, SECONDS)
      .writeTimeout(60, SECONDS)
      .connectTimeout(30, SECONDS)
      .addInterceptor(DefaultInterceptor())

  val retrofitBuilder = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(GsonConverterFactory.create(defaultGson))

  inline fun <reified T> service(): T {
    val token = TOKEN
    if (token != null)
      httpBuilder.addInterceptor(AuthTokenInterceptor(token))

    return retrofitBuilder
      .client(httpBuilder.build())
      .build()
      .create(T::class.java)
  }

  companion object {
    var TOKEN: String? = null
  }
}


