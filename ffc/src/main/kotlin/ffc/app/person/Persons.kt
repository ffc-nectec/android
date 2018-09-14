package ffc.app.person

import ffc.app.search.PersonSearcher
import ffc.entity.Person
import ffc.entity.ThaiCitizenId
import ffc.util.RepoCallback
import org.joda.time.LocalDate

interface Persons {

    fun person(personId: String, callback: (Person?, Throwable?) -> Unit)

    fun add(person: Person, callback: (Person?, Throwable?) -> Unit)
}

private class InMemoryPersons : Persons, PersonSearcher {
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

    override fun person(personId: String, callback: (Person?, Throwable?) -> Unit) {
        val person = repository.find { person -> person.id == personId }
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
