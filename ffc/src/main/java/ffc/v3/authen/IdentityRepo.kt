package ffc.v3.authen

import android.content.Context
import ffc.entity.Organization
import ffc.v3.util.get
import ffc.v3.util.put
import org.jetbrains.anko.defaultSharedPreferences

interface IdentityRepo {

    var org: Organization?

    var token: String?
}

private class PreferenceIdentityRepo(context: Context) : IdentityRepo {
    var preference = context.defaultSharedPreferences
    override var org: Organization?
        get() = preference.get<Organization>("org")
        set(value) {
            preference.edit().put("org", value).apply()
        }
    override var token: String?
        get() = preference.getString("token", null)
        set(value) {
            preference.edit().putString("token", value).apply()
        }
}

fun getIdentityRepo(context: Context): IdentityRepo = PreferenceIdentityRepo(context)
