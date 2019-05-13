/*
 * Copyright (c) 2019 NECTEC
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

package ffc.app.health.analyze

import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.mockRepository
import ffc.app.util.RepoCallback
import ffc.entity.Person
import ffc.entity.healthcare.Severity
import ffc.entity.healthcare.analyze.HealthAnalyzer
import ffc.entity.healthcare.analyze.HealthChecked
import ffc.entity.healthcare.analyze.HealthIssue
import ffc.entity.healthcare.analyze.HealthProblem
import retrofit2.Call
import retrofit2.dsl.enqueue
import retrofit2.http.GET
import retrofit2.http.Path

interface HealthIssues {

    fun issueOf(person: Person, dsl: RepoCallback<Collection<HealthIssue>>.() -> Unit)
}

fun healthIssues(): HealthIssues = if (mockRepository) DummyHealthIssue() else ApiHealthIssues()

internal class DummyHealthIssue : HealthIssues {

    override fun issueOf(person: Person, dsl: RepoCallback<Collection<HealthIssue>>.() -> Unit) {
        val callback = RepoCallback<Collection<HealthIssue>>().apply(dsl)
        val issues = listOf(
            HealthProblem(HealthIssue.Issue.HT, Severity.HI),
            HealthProblem(HealthIssue.Issue.DM, Severity.LOW),
            HealthProblem(HealthIssue.Issue.CVD, Severity.VERY_HI),
            HealthProblem(HealthIssue.Issue.ACTIVITIES, Severity.MID),
            HealthChecked(HealthIssue.Issue.FALL_RISK),
            HealthChecked(HealthIssue.Issue.CATARACT, haveIssue = true)
        )
        callback.onFound!!.invoke(issues)
    }
}

internal class ApiHealthIssues : HealthIssues {

    val api = FfcCentral().service<HealthIssueServiceApi>()

    override fun issueOf(person: Person, dsl: RepoCallback<Collection<HealthIssue>>.() -> Unit) {
        val callback = RepoCallback<Collection<HealthIssue>>().apply(dsl)
        api.get(person.orgId!!, person.id).enqueue {
            onSuccess {
                callback.onFound!!.invoke(body()!!.result.values)
            }
            onClientError {
                callback.onNotFound!!.invoke()
            }
            onServerError {
                callback.onFail!!.invoke(ApiErrorException(this))
            }
            onFailure {
                callback.onFail!!.invoke(it)
            }
        }
    }
}

interface HealthIssueServiceApi {

    @GET("org/{orgId}/person/{personId}/healthanalyze")
    fun get(@Path("orgId") orgId: String, @Path("personId") personId: String): Call<HealthAnalyzer>
}
