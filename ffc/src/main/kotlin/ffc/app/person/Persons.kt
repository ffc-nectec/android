package ffc.app.person

import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.isDev
import ffc.app.util.RepoCallback
import ffc.app.util.TaskCallback
import ffc.entity.Lang
import ffc.entity.Organization
import ffc.entity.Person
import ffc.entity.ThaiCitizenId
import ffc.entity.healthcare.Icd10
import ffc.entity.util.generateTempId
import org.joda.time.LocalDate
import retrofit2.dsl.enqueue
import retrofit2.dsl.then

interface Persons {

    fun person(personId: String, dsl: RepoCallback<Person>.() -> Unit)

    fun add(person: Person, callback: (Person?, Throwable?) -> Unit)
}

interface PersonManipulator {

    fun update(callbackDsl: TaskCallback<Person>.() -> Unit)
}

private class InMemoryPersons : Persons {
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

    override fun add(person: Person, callback: (Person?, Throwable?) -> Unit) {
        repository.add(person)
        callback(person, null)
    }

    companion object {
        val repository: MutableList<Person> = mutableListOf(mockPerson)
    }
}

private class ApiPersons(val orgId: String) : Persons {

    val api = FfcCentral().service<PersonsApi>()

    override fun add(person: Person, callback: (Person?, Throwable?) -> Unit) {
        api.post(orgId, person).then {
            callback(it, null)
        }.catch { res, t ->
            res?.let { callback(null, ApiErrorException(it)) }
            t?.let { callback(null, it) }
        }
    }

    override fun person(personId: String, dsl: RepoCallback<Person>.() -> Unit) {
        val callback = RepoCallback<Person>().apply(dsl)
        api.get(orgId, personId).enqueue {
            always { callback.always?.invoke() }
            onSuccess {
                callback.onFound!!.invoke(body()!!)
            }
            onError {
                if (code() == 404)
                    callback.onNotFound!!.invoke()
                else
                    callback.onFail!!.invoke(ApiErrorException(this))
            }
            onFailure { callback.onFail!!.invoke(it) }
        }
    }
}

class DummyPersonManipulator(val orgId: String, val person: Person) : PersonManipulator {
    override fun update(callbackDsl: TaskCallback<Person>.() -> Unit) {
        val callback = TaskCallback<Person>().apply(callbackDsl)
        callback.result(person)
    }
}

class ApiPersonManipulator(val orgId: String, val person: Person) : PersonManipulator {

    val api = FfcCentral().service<PersonsApi>()

    override fun update(callbackDsl: TaskCallback<Person>.() -> Unit) {
        val callback = TaskCallback<Person>().apply(callbackDsl)
        api.put(orgId, person).enqueue {
            onSuccess {
                callback.result(body()!!)
            }
            onError {
                callback.expception!!.invoke(ApiErrorException(this))
            }
            onFailure {
                callback.expception!!.invoke(it)
            }
        }
    }
}

val mockPerson = Person("5b9770e029191b0004c91a56").apply {
    birthDate = LocalDate.parse("1988-02-15")
    prename = "นาย"
    firstname = "พิรุณ"
    lastname = "พานิชผล"
    sex = Person.Sex.MALE
    identities.add(ThaiCitizenId("1145841548789"))
    death = Person.Death(LocalDate.now(),
        Icd10("ไข้หวัดใหญ่ร่วมกับปอดบวม ตรวจพบไวรัสไข้หวัดใหญ่ชนิดอื่น", "J10.0").apply {
            translation[Lang.en] = "Influenza with pneumonia, other influenza virus identified"
        },
        Icd10("เบาหวานชนิดพึ่งอินซูลิน", "E10").apply {
            translation[Lang.en] = "Insulin-dependent diabetes mellitus"
        }
    )
    relationships.add(Person.Relationship(Person.Relate.Mother, generateTempId(), "ปานแก้ว พานิชผล"))
    relationships.add(Person.Relationship(Person.Relate.Father, generateTempId(), "พิภพ พานิชผล"))
    relationships.add(Person.Relationship(Person.Relate.Married, generateTempId(), "พรทิพา โชคสูงเนิน"))

}

internal fun Person.manipulator(org: Organization): PersonManipulator {
    return if (isDev)
        DummyPersonManipulator(org.id, this)
    else
        ApiPersonManipulator(org.id, this)
}

fun Person.pushTo(org: Organization, callbackDsl: TaskCallback<Person>.() -> Unit) {
    manipulator(org).update(callbackDsl)
}

fun persons(orgId: String): Persons = if (isDev) InMemoryPersons() else ApiPersons(orgId)
