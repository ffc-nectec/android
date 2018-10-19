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

import ffc.api.FfcCentral
import ffc.app.auth.exception.LoginFailureException
import ffc.app.util.RepoCallback
import ffc.entity.Organization
import retrofit2.dsl.then

interface Orgs {

    fun myOrg(dsl: RepoCallback<List<Organization>>.() -> Unit)

    fun org(query: String, dsl: RepoCallback<List<Organization>>.() -> Unit)
}

private class OrgsImpl : Orgs {

    private val orgService = FfcCentral().service<OrgService>()

    override fun myOrg(dsl: RepoCallback<List<Organization>>.() -> Unit) {
        val callback = RepoCallback<List<Organization>>().apply(dsl)
        orgService.myOrg().then {
            callback.onFound?.invoke(it)
        }.catch { res, t ->
            res?.let {
                if (it.code() == 404)
                    callback.onNotFound?.invoke()
            }
            t?.let {
                callback.onFail?.invoke(LoginFailureException(it.message ?: "Something wrong"))
            }
        }.always {
            callback.always?.invoke()
        }
    }

    override fun org(query: String, dsl: RepoCallback<List<Organization>>.() -> Unit) {
        val callback = RepoCallback<List<Organization>>().apply(dsl)
        orgService.listOrgs(query).then {
            if (it.isNotEmpty())
                callback.onFound?.invoke(it)
            else
                callback.onNotFound?.invoke()
        }.catch { res, t ->
            res?.let {
                callback.onNotFound?.invoke()
            }
            t?.let {
                callback.onFail?.invoke(LoginFailureException(it.message ?: "Something wrong"))
            }
        }.always {
            callback.always?.invoke()
        }
    }
}

fun orgs(): Orgs = OrgsImpl()
