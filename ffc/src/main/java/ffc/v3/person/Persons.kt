package ffc.v3.person

import ffc.entity.Person
import ffc.entity.ThaiCitizenId
import org.joda.time.LocalDate

interface Persons {

    fun person(personId: String, callback: (Person?, Throwable?) -> Unit)

    fun add(person: Person, callback: (Person?, Throwable?) -> Unit)
}

private class InMemoryPersons : Persons {
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

val mockPerson = Person().apply {
    birthDate = LocalDate.parse("1988-02-15")
    prename = "นาย"
    firstname = "พิรุณ"
    lastname = "พานิชผล"
    sex = Person.Sex.MALE
    identities.add(ThaiCitizenId("1145841548789"))
}

fun persons(): Persons = InMemoryPersons()
