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

package ffc.app.search

import ffc.api.FfcCentral
import ffc.api.ServerErrorException
import ffc.app.isDev
import ffc.app.person.mockPerson
import ffc.app.util.RepoCallback
import ffc.entity.Person
import retrofit2.Call
import retrofit2.dsl.enqueue
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonSearcher {

    fun search(query: String, dsl: RepoCallback<List<Person>>.() -> Unit)
}

fun personSearcher(orgId : String): PersonSearcher = if (isDev) InMemoryPersonSearcher() else ApiPersonSearcher(orgId)

private class InMemoryPersonSearcher : PersonSearcher {

    val repository = listOf(mockPerson)

    override fun search(query: String, dsl: RepoCallback<List<Person>>.() -> Unit) {
        val callback = RepoCallback<List<Person>>().apply(dsl)
        callback.always?.invoke()
        repository.filter { it.name.contains(query) }.let {
            if (it.isNotEmpty())
                callback.onFound?.invoke(it)
            else
                callback.onNotFound?.invoke()
        }
    }
}

private class ApiPersonSearcher(val orgId: String) : PersonSearcher {

    val api = FfcCentral().service<PersonSearchApi>()

    override fun search(query: String, dsl: RepoCallback<List<Person>>.() -> Unit) {
        val callback = RepoCallback<List<Person>>().apply(dsl)
        api.getQuery(orgId, query).enqueue {
            always { callback.always?.invoke() }
            onSuccess {
                val list = body()!!
                if (list.isNotEmpty()) {
                    callback.onFound!!.invoke(list)
                } else {
                    callback.onNotFound!!.invoke()
                }
            }
            onError { callback.onFail!!.invoke(ServerErrorException(this)) }
            onFailure { callback.onFail!!.invoke(it) }
        }
    }
}

interface PersonSearchApi {

    @GET("org/{orgId}/person")
    fun getQuery(@Path("orgId") orgId: String, @Query("query") query: String): Call<List<Person>>
}
