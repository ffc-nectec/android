package ffc.app.healthservice.analyze

import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.isDev
import ffc.app.util.RepoCallback
import ffc.entity.Person
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

fun healthIssues(): HealthIssues = if (isDev) DummyHealthIssue() else ApiHealthIssues()

internal class DummyHealthIssue : HealthIssues {

    override fun issueOf(person: Person, dsl: RepoCallback<Collection<HealthIssue>>.() -> Unit) {
        val callback = RepoCallback<Collection<HealthIssue>>().apply(dsl)
        val issues = listOf(
            HealthProblem(HealthIssue.Issue.HT, HealthIssue.Severity.HI),
            HealthProblem(HealthIssue.Issue.DM, HealthIssue.Severity.LOW),
            HealthProblem(HealthIssue.Issue.CVD, HealthIssue.Severity.VERY_HI),
            HealthProblem(HealthIssue.Issue.ACTIVITIES, HealthIssue.Severity.MID),
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
