package ffc.app.person.genogram

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.otaliastudios.zoom.ZoomLayout
import de.hdodenhof.circleimageview.CircleImageView
import ffc.android.load
import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.person.personId
import ffc.entity.Organization
import ffc.genogram.Family
import ffc.genogram.GenderLabel
import ffc.genogram.Person
import ffc.genogram.android.Families
import ffc.genogram.android.GenogramNodeBuilder
import ffc.genogram.android.GenogramView
import retrofit2.Call
import retrofit2.dsl.enqueue
import retrofit2.http.GET
import retrofit2.http.Path
import ffc.entity.Person as FFCPerson

class GenogramActivity : FamilyFolderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genogram)

        val personId = intent.personId

        val families = ApiFamilies(org!!, personId!!)
        families.family {
            onSuccess {
                val view = GenogramView(this@GenogramActivity)
                view.nodeBuilder = SimplePersonViewHolder()
                view.drawFamily(it)

                val container = findViewById<ZoomLayout>(R.id.container)
                container.addView(view)
                Handler().postDelayed({ container.zoomOut() }, 150)
            }
        }
    }
}

class SimplePersonViewHolder : GenogramNodeBuilder {
    override fun viewFor(person: Person, context: Context, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view = when (person.gender) {
            GenderLabel.MALE -> layoutInflater.inflate(R.layout.genogram_node_male_item, parent, false)
            GenderLabel.FEMALE -> layoutInflater.inflate(R.layout.genogram_node_female_item, parent, false)
        }
        val icon = view.findViewById<View>(R.id.icon)
        val identicon = Uri.parse("https://identicon.org?s=128&t=ffc-${person.idCard}")
        when (icon) {
            is ImageView -> icon.load(identicon)
            is CircleImageView -> icon.load(identicon)
        }
        icon.setOnClickListener {
            Toast.makeText(context, "Click ${person.firstname} ${person.lastname}", Toast.LENGTH_SHORT).show()
        }
        val name = view.findViewById<TextView>(R.id.name)
        name.text = person.firstname
        return view
    }
}

class ApiFamilies(val org: Organization, val personId: String) : Families {

    val api = FfcCentral().service<GenogramApi>()

    override fun family(callbackDsl: Families.Callback.() -> Unit) {
        val callback = Families.Callback().apply(callbackDsl)

//        val call = if (isDev) api.demo() else api.get(org.id, personId)
        val call = api.demo()
        call.enqueue {
            onSuccess { callback.onSuccess(body()!!) }
            onError { callback.onFail(ApiErrorException(this)) }
            onFailure { callback.onFail(it) }
        }
    }
}

interface GenogramApi {

    @GET("org/1/person/genogram/demo")
    fun demo(): Call<Family>

    @GET("org/{orgId}/person/{personId}/genogram")
    fun get(@Path("orgId") orgId: String, @Path("personId") personId: String): Call<Family>
}
