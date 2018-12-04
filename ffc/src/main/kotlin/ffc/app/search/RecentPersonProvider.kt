package ffc.app.search

import android.content.Context
import ffc.android.get
import ffc.android.put
import ffc.entity.Organization
import ffc.entity.Person
import java.util.LinkedList

class RecentPersonProvider(val context: Context, val org: Organization) {

    private val preference by lazy {
        context.getSharedPreferences("recentPerson-${org.id}", Context.MODE_PRIVATE)
    }

    private var persons: LinkedList<Person>
        set(value) {
            preference.edit().put("person", value).apply()
        }
        get() = preference.get("person") ?: LinkedList()

    fun getRecentPerson(): List<Person> {
        return persons
    }

    fun saveRecentPerson(recent: Person) {
        val list = persons
        if (list.contains(recent)) {
            list.remove(recent)
        }
        list.addFirst(recent)
        persons = list
    }

    fun clearRecentHistory() {
        persons = LinkedList()
    }
}
