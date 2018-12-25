package ffc.app.health.service.community

import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.mockRepository
import ffc.app.util.RepoCallback
import ffc.entity.Organization
import ffc.entity.Template
import retrofit2.Call
import retrofit2.dsl.enqueue
import retrofit2.http.GET
import retrofit2.http.Path

interface TemplateApi {

    @GET("org/{orgId}/template")
    fun getAll(@Path("orgId") orgId: String): Call<List<Template>>
}

interface Templates {

    fun all(callbackDsl: RepoCallback<List<Template>>.() -> Unit)
}

internal fun templatesOf(org: Organization): Templates = if (mockRepository) DummyTemplate() else ApiTemplates(org.id)

private class ApiTemplates(val orgId: String) : Templates {

    private val api by lazy { FfcCentral().service<TemplateApi>() }

    override fun all(callbackDsl: RepoCallback<List<Template>>.() -> Unit) {
        val callback = RepoCallback<List<Template>>().apply(callbackDsl)

        api.getAll(orgId).enqueue {
            onSuccess {
                callback.onFound!!.invoke(body()!!)
            }

            onError {
                if (code() == 404)
                    callback.onNotFound?.invoke()
                else
                    callback.onFail!!.invoke(ApiErrorException(this))
            }
            onFailure { callback.onFail!!.invoke(it) }
        }
    }
}

private class DummyTemplate() : Templates {

    override fun all(callbackDsl: RepoCallback<List<Template>>.() -> Unit) {
        val callback = RepoCallback<List<Template>>().apply(callbackDsl)
        callback.onFound!!.invoke(listOf(
            Template("Hello", "HealthCareService.syntom"),
            Template("World", "HealthCareService.syntom"),
            Template("FFC", "HealthCareService.syntom")
        ))
    }
}
