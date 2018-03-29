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

package ffc.v3

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

class FfcCentral(url: String = "http://188.166.249.72/v0/") {

  val httpBuilder: OkHttpClient.Builder =
    OkHttpClient.Builder()
      .readTimeout(60, SECONDS)
      .writeTimeout(60, SECONDS)
      .connectTimeout(30, SECONDS)
      .addInterceptor(DefaultInterceptor())

  val retrofitBuilder = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(GsonConverterFactory.create())

  inline fun <reified T> service(): T {
    if (TOKEN != null)
      httpBuilder.addInterceptor(AuthTokenInterceptor(TOKEN))

    return retrofitBuilder
      .client(httpBuilder.build())
      .build()
      .create(T::class.java)
  }

  companion object {
    val TOKEN: String? = null
  }
}

inline fun <reified T> Call<T>.then(crossinline task: (Response<T>?, Throwable?) -> Unit) {
  enqueue(object : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
      task(response, null)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
      task(null, t)
    }
  })
}
