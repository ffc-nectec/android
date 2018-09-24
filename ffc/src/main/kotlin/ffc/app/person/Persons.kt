package ffc.app.person

import ffc.app.search.PersonSearcher
import ffc.app.util.RepoCallback
import ffc.entity.Person
import ffc.entity.ThaiCitizenId
import org.joda.time.LocalDate

interface Persons {

    fun person(personId: String, dsl: RepoCallback<Person>.() -> Unit)

    fun add(person: Person, callback: (Person?, Throwable?) -> Unit)
}

private class InMemoryPersons : Persons, PersonSearcher {
    override fun person(personId: String, dsl: RepoCallback<Person>.() -> Unit) {
        val callback = RepoCallback<Person>().apply(dsl)
        callback.always?.invoke()
        val person = repository.firstOrNull { person -> person.id == personId }
        if (person != null) {
            callback.onFound!!.invoke(person)
        } else {
            callback.onNotFound!!.invoke()
        }
    }

    override fun search(query: String, dsl: RepoCallback<List<Person>>.() -> Unit) {
        val callback = RepoCallback<List<Person>>().apply(dsl)
        callback.always?.invoke()
        repository.filter { it.name.contains(query) }.let {
            if (it.isNotEmpty())
                callback.onFound?.invoke(it)
            else
                callback.onNotFound?.invoke()
        }
    }

    override fun add(person: Person, callback: (Person?, Throwable?) -> Unit) {
        repository.add(person)
        callback(person, null)
    }

    companion object {
        val repository: MutableList<Person> = mutableListOf(mockPerson)
    }
}

val mockPerson = Person("5b9770e029191b0004c91a56").apply {
    birthDate = LocalDate.parse("1988-02-15")
    prename = "นาย"
    firstname = "พิรุณ"
    lastname = "พานิชผล"
    sex = Person.Sex.MALE
    identities.add(ThaiCitizenId("1145841548789"))
}

fun persons(): Persons = InMemoryPersons()

fun personSearcher(): PersonSearcher = InMemoryPersons()
