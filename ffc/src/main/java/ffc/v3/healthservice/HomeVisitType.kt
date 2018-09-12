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

package ffc.v3.healthservice

import android.content.Context
import ffc.entity.gson.parseTo
import ffc.entity.gson.toJson
import ffc.entity.healthcare.CommunityServiceType
import ffc.v3.android.connectivityManager
import ffc.v3.api.FfcCentral
import ffc.v3.isConnected
import org.jetbrains.anko.toast
import retrofit2.dsl.enqueue
import java.io.File
import java.io.IOException

interface HomeVisitType {

    fun all(res: (List<CommunityServiceType>, Throwable?) -> Unit)
}

internal fun homeVisitType(context: Context): HomeVisitType = HomeHealtServiceImpl(context)

private class HomeHealtServiceImpl(val context: Context) : HomeVisitType {

    private val localFile = File(context.filesDir, "homevisittype.json")

    val api by lazy { FfcCentral().service<CommunityServicesApi>() }

    var loading = false

    init {
        if (homeVisitType.isEmpty()) {
            if (context.connectivityManager.isConnected) {
                loading = true
                api.getHomeHealthService().enqueue {
                    always {
                        loading = false
                    }
                    onSuccess {
                        context.toast("Request")
                        homeVisitType = body()!!
                        tempRes?.invoke(homeVisitType, null)
                        try {
                            localFile.createNewFile()
                        } catch (ignore: IOException) {
                        }
                        localFile.writeText(homeVisitType.toJson())
                    }
                    onRedirect {
                        context.toast("Redirect")
                    }
                    onError {
                        tempRes?.invoke(listOf(), IllegalStateException("Request Error"))
                    }
                    onFailure {
                        tempRes?.invoke(listOf(), it)
                    }
                }
            }
            if (localFile.exists()) {
                homeVisitType = localFile.readText().parseTo()
            }
        }
    }

    var tempRes: ((List<CommunityServiceType>, Throwable?) -> Unit)? = null

    override fun all(res: (List<CommunityServiceType>, Throwable?) -> Unit) {
        if (loading) {
            tempRes = res
        } else {
            res(homeVisitType, null)
        }
    }

    companion object {
        var homeVisitType = listOf<CommunityServiceType>()
    }
}

val notDefineCommunityService = CommunityServiceType("null", "NULL")
