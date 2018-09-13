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

package ffc.app.authen

import android.content.Context
import ffc.entity.Organization
import ffc.entity.User
import ffc.entity.gson.toJson
import ffc.util.get
import ffc.util.put
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

    override fun toString(): String {
        return "org = ${org?.toJson()}\nuser = ${user?.toJson()}\ntoken = $token"
    }
}

fun getIdentityRepo(context: Context): IdentityRepo = PreferenceIdentityRepo(context)
