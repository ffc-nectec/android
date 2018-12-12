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

package ffc.app.health.diagnosis

import android.content.Context
import ffc.android.assetAs
import ffc.android.connectivityManager
import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.isConnected
import ffc.app.util.RepoCallback
import ffc.entity.gson.parseTo
import ffc.entity.gson.toJson
import ffc.entity.healthcare.Disease
import ffc.entity.healthcare.Icd10
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.dsl.enqueue
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.io.IOException

internal interface Diseases {

    fun all(res: RepoCallback<List<Disease>>.() -> Unit)
}

internal fun diseases(context: Context): Diseases = MockDiseases(context)

private class ApiDisease(context: Context) : Diseases {

    private val localFile = File(context.filesDir, "disease.json")
    private var loading = false
    private var tmpCallback: RepoCallback<List<Disease>>? = null
    private val api = FfcCentral().service<DiseaseApi>()

    init {
        if (context.connectivityManager.isConnected) {
            loading = true
            loadDisease(mutableListOf(), 1, 5000) {
                tmpCallback?.onFound?.invoke(it)
                doAsync {
                    try {
                        localFile.createNewFile()
                    } catch (ignore: IOException) {
                        //file already created
                    }
                    localFile.writeText(it.toJson())
                }
            }
        }
        if (localFile.exists()) {
            disease = localFile.readText().parseTo()
        }
    }

    fun loadDisease(disease: MutableList<Disease>, currentPage: Int, perPage: Int, onFinish: (List<Disease>) -> Unit) {
        api.get(currentPage, perPage).enqueue {
            always {
                tmpCallback?.always?.invoke()
                loading = false
            }
            onSuccess {
                disease.addAll(body()!!)
                if (currentPage == page?.last) {
                    onFinish(disease)
                } else {
                    loadDisease(disease, page!!.next, page!!.perPage, onFinish)
                }
            }
            onError {
                tmpCallback?.onFail!!.invoke(ApiErrorException(this))
            }
            onFailure {
                tmpCallback?.onFail!!.invoke(it)
            }
        }
    }

    override fun all(res: RepoCallback<List<Disease>>.() -> Unit) {
        val callback = RepoCallback<List<Disease>>().apply(res)
        if (loading) {
            tmpCallback = callback
        } else {
            callback.always?.invoke()
            if (disease.isNotEmpty()) {
                callback.onFound!!.invoke(disease)
            } else {
                callback.onNotFound!!.invoke()
            }
        }
    }

    companion object {
        var disease = listOf<Disease>()
    }
}

private class MockDiseases(val context: Context) : Diseases {

    init {
        if (disease.isEmpty()) {
            disease = context.assetAs<List<Icd10>>("lookups/Disease.json")
        }
    }

    override fun all(res: RepoCallback<List<Disease>>.() -> Unit) {
        val callback = RepoCallback<List<Disease>>().apply(res)
        callback.onFound!!.invoke(disease)
    }

    companion object {
        internal var disease: List<Disease> = listOf()
    }
}

interface DiseaseApi {

    @GET("disease")
    fun get(@Query("page") page: Int = 1, @Query("per_page") perPage: Int = 10000): Call<List<Disease>>
}
