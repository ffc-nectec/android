package ffc.v3.healthservice

internal interface Diseases {

    fun all(res: (List<Diseases>, Throwable?) -> Unit)
}

internal fun diseases(): Diseases = MockDiseases()

private class MockDiseases : Diseases {

    override fun all(res: (List<Diseases>, Throwable?) -> Unit) {
        res(listOf(), null)
    }
}
