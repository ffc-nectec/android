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

package ffc.app.health.service.community

import android.content.Context
import ffc.android.connectivityManager
import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.isConnected
import ffc.app.util.RepoCallback
import ffc.entity.gson.parseTo
import ffc.entity.gson.toJson
import ffc.entity.healthcare.CommunityService
import retrofit2.dsl.enqueue
import java.io.File
import java.io.IOException

interface CommunityServiceTypes {

    fun all(callbackDsl: RepoCallback<List<CommunityService.ServiceType>>.() -> Unit)
}

internal fun communityServiceTypes(context: Context): CommunityServiceTypes = CommunityServiceTypesImpl(context)

private class CommunityServiceTypesImpl(val context: Context) : CommunityServiceTypes {

    private val localFile = File(context.filesDir, "homevisittype.json")
    private val api by lazy { FfcCentral().service<CommunityServicesApi>() }
    private var loading = false
    private var tmpCallback: RepoCallback<List<CommunityService.ServiceType>>? = null

    init {
        if (serviceType.isEmpty()) {
            if (context.connectivityManager.isConnected) {
                loading = true
                api.getHomeHealthService().enqueue {
                    always {
                        tmpCallback?.always?.invoke()
                        loading = false
                    }
                    onSuccess {
                        serviceType = body()!!
                        tmpCallback?.onFound?.invoke(serviceType)
                        try {
                            localFile.createNewFile()
                        } catch (ignore: IOException) {
                        }
                        localFile.writeText(serviceType.toJson())
                    }
                    onError {
                        tmpCallback?.onFail!!.invoke(ApiErrorException(this))
                    }
                    onFailure {
                        tmpCallback?.onFail!!.invoke(it)
                    }
                }
            }
            if (localFile.exists()) {
                serviceType = localFile.readText().parseTo()
            }
        }
    }

    override fun all(callbackDsl: RepoCallback<List<CommunityService.ServiceType>>.() -> Unit) {
        val callback = RepoCallback<List<CommunityService.ServiceType>>().apply(callbackDsl)
        if (loading) {
            tmpCallback = callback
        } else {
            callback.always?.invoke()
            if (serviceType.isNotEmpty()) {
                callback.onFound!!.invoke(serviceType)
            } else {
                callback.onNotFound!!.invoke()
            }
        }
    }

    companion object {
        var serviceType = listOf<CommunityService.ServiceType>()
    }
}
