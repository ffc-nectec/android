package ffc.v3.authen

import android.content.Context
import ffc.entity.Organization
import ffc.entity.User
import ffc.v3.util.get
import ffc.v3.util.put
import org.jetbrains.anko.defaultSharedPreferences

interface IdentityRepo {

    var org: Organization?

    var token: String?

    var user: User?
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

    override var user: User?
        get() = preference.get<User>("user")
        set(value) {
            preference.edit().put("user", value).apply()
        }
}

fun getIdentityRepo(context: Context): IdentityRepo = PreferenceIdentityRepo(context)
