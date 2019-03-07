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

package ffc.api

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import ffc.android.put
import ffc.app.BuildConfig
import ffc.app.dev
import ffc.entity.gson.ffcGson
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.defaultSharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit.SECONDS

private var url_staging = "https://api-staging.ffc.in.th/v1/"
private var url_beta = "https://api-beta.ffc.in.th/v1/"
private var url_production = "https://api.ffc.in.th/v1/"
private var url_debug = url_staging

class FfcCentral(url: String = FfcCentral.url, val gson: Gson = ffcGson) {

    val retrofitBuilder = Retrofit.Builder().baseUrl(url)!!

    inline fun <reified T> service(): T {
        val httpBuilder = OkHttpClient.Builder().apply {
            readTimeout(30, SECONDS)
            writeTimeout(30, SECONDS)
            connectTimeout(15, SECONDS)
            addInterceptor(DefaultInterceptor())
            dev { addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }) }
            cache?.let { cache(it) }
            token?.let { addInterceptor(AuthTokenInterceptor(it)) }
        }

        return retrofitBuilder
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpBuilder.build())
            .build()
            .create(T::class.java)
    }

    companion object {
        private val defaultUrl: String = if (BuildConfig.BUILD_TYPE == "release") url_production else url_debug

        var token: String? = null
        var cache: Cache? = null
        var url: String = defaultUrl
            private set

        fun loadUrl(context: Context) {
            if (BuildConfig.DEBUG) {
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                FfcCentral.url = context.defaultSharedPreferences.getString("url", defaultUrl)
                Timber.d("url=$url")
            }
        }

        fun saveUrl(context: Context, url: Uri) {
            require(BuildConfig.DEBUG) { "Can't url on not debuggable app" }
            require(url.scheme == "https") { "url must be http" }
            require(url.path.endsWith("/")) { "url must end with /" }

            context.defaultSharedPreferences.edit().put("url", url.toString()).apply()
            FfcCentral.url = url.toString()
        }
    }
}
