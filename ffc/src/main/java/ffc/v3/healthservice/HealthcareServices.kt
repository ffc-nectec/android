package ffc.v3.healthservice

import ffc.entity.Person
import ffc.entity.healthcare.HealthCareService
import java.util.concurrent.ConcurrentHashMap

internal interface HealthcareServices {

    fun add(services: HealthCareService, callback: (HealthCareService?, Throwable?) -> Unit)
}

private class InMemoryHealthCareServices(val person: Person) : HealthcareServices {

    override fun add(services: HealthCareService, callback: (HealthCareService?, Throwable?) -> Unit) {
        require(services.patientId == person.id) { "Not match patinet id" }
        val list = repository[person.id] ?: mutableListOf()
        list.add(services)
        repository[person.id] = list
        callback(services, null)
    }

    companion object {
        val repository: ConcurrentHashMap<String, MutableList<HealthCareService>> = ConcurrentHashMap()
    }
}

internal fun Person.healthCareServices(): HealthcareServices = InMemoryHealthCareServices(this)
