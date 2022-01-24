package ffc.app.person.genogram

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.auth.legal.LegalAgreementApi
import ffc.entity.Organization
import ffc.entity.gson.ffcGson
import ffc.genogram.Family
import ffc.genogram.Person
import ffc.genogram.genogramGson
import retrofit2.Call
import retrofit2.dsl.enqueue
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Type
import kotlin.properties.Delegates

internal class ApiFamilies(val org: Organization, val personId: String) : genogramCollects {

//    private val familyGson = GsonBuilder()
//        .registerTypeAdapter(Person::class.java, GenogramPersonDeserializer())
//        .create()
    //private val api = FfcCentral(gson = familyGson).service<GenogramApi>()

    var api = FfcCentral().service<GenogramApi>()
    override fun genogramCollects(callbackDsl: genogramCollects.Callback.() -> Unit) {
        val callback = genogramCollects.Callback().apply(callbackDsl)

        val call = api.get(org.id, personId)
        call.enqueue {
            onSuccess { callback.onSuccess(body()!!) }
            onError { callback.onFail(ApiErrorException(this)) }
            onFailure { callback.onFail(it) }
        }
    }
}

//private class GenogramPersonDeserializer : JsonDeserializer<Person> {
//
//    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Person {
//        val person = genogramGson.fromJson<Person>(json, typeOfT)
//        person.properties = ffcGson.fromJson<ffc.entity.Person>(
//            json.asJsonObject.get("properties"),
//            ffc.entity.Person::class.java)
//        return person
//    }
//}

interface genogramCollects {
    fun genogramCollects(callbackDsl: Callback.() -> Unit)
    class Callback {
        lateinit var onSuccess: ((List<genogramCollect>) -> Unit)
            private set
        lateinit var onFail: ((Throwable?) -> Unit)
            private set

        fun onSuccess(onFound: (List<genogramCollect>) -> Unit) {
            this.onSuccess = onFound
        }

        fun onFail(onFail: (Throwable?) -> Unit) {
            this.onFail = onFail
        }
    }
}
interface GenogramApi {

//    @GET("org/1/person/genogram/demo")
//    fun demo(): Call<Family>

//    @GET("org/{orgId}/person/{personId}/genogram")
//    fun get(@Path("orgId") orgId: String, @Path("personId") personId: String): Call<Family>

    @GET("org/{orgId}/person/{personId}/genogram/collect")
    fun get(@Path("orgId") orgId: String, @Path("personId") personId: String): Call<List<genogramCollect>>
}
class genogramCollect{

     var orgId: String?=null
     var identities: List<identities>?=null
     var avatarUrl: String? = null
     var death: death?=null
     //var chronics:
     var link:link? =null
     var relationships:List<relationships>?=null
    lateinit var prename : String
    lateinit var firstname : String
    lateinit var lastname : String
    lateinit var sex : String
    lateinit var birthDate : String
    lateinit var houseId : String
    lateinit var id : String
    lateinit var type : String
    lateinit var timestamp : String
}
class identities{
     var type:String?=null
     var id:String?=null
}
class link {
    var isSynced:Boolean = true;
    lateinit var lastSync:String
    lateinit var system:String
    lateinit var keys:keys
}
class keys{
    lateinit var pcucodeperson:String
    lateinit var pid:String
    lateinit var hcode:String
    lateinit var marystatus:String
    lateinit var marystatusth:String
    lateinit var famposname:String
    lateinit var familyposition:String
    lateinit var familyno:String
    lateinit var fatherid:String
    lateinit var father:String
    lateinit var motherid:String
    lateinit var mother:String
    lateinit var rightcode:String
    lateinit var rightno:String
    lateinit var hosmain:String
    lateinit var hossub:String
}
class relationships{
    lateinit var relate: String
    lateinit var id:String
    lateinit var name:String
    var age:Int=0
}
class death {
    var date:String?=null
    var causes:List<causes>?=null
}
class causes{
    var icd10 :String ?=null
    var isEpimedic :String ?=null
    var isChronic : String ?=null
    var isNCD : String ?=null
    var type : String ?=null
    var translation: translation?=null
    var id: String? = null
    var name: String? =null
}
class translation {
    var en: String ?=null;
}
