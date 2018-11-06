package ffc.app.person.genogram

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.isDev
import ffc.entity.Organization
import ffc.entity.gson.ffcGson
import ffc.genogram.Family
import ffc.genogram.Person
import ffc.genogram.android.Families
import ffc.genogram.genogramGson
import retrofit2.Call
import retrofit2.dsl.enqueue
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Type

internal class ApiFamilies(val org: Organization, val personId: String) : Families {

    private val familyGson = GsonBuilder()
        .registerTypeAdapter(Person::class.java, GenogramPersonDeserializer())
        .create()
    private val api = FfcCentral(gson = familyGson).service<GenogramApi>()

    override fun family(callbackDsl: Families.Callback.() -> Unit) {
        val callback = Families.Callback().apply(callbackDsl)

        val call = if (isDev) api.demo() else api.get(org.id, personId)
        call.enqueue {
            onSuccess { callback.onSuccess(body()!!) }
            onError { callback.onFail(ApiErrorException(this)) }
            onFailure { callback.onFail(it) }
        }
    }
}

private class GenogramPersonDeserializer : JsonDeserializer<Person> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Person {
        val person = genogramGson.fromJson<Person>(json, typeOfT)
        person.properties = ffcGson.fromJson<ffc.entity.Person>(
            json.asJsonObject.get("properties"),
            ffc.entity.Person::class.java)
        return person
    }
}

private interface GenogramApi {

    @GET("org/1/person/genogram/demo")
    fun demo(): Call<Family>

    @GET("org/{orgId}/person/{personId}/genogram")
    fun get(@Path("orgId") orgId: String, @Path("personId") personId: String): Call<Family>
}
