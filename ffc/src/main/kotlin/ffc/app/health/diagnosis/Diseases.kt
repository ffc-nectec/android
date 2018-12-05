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

package ffc.app.health.diagnosis

import android.content.Context
import ffc.android.assetAs
import ffc.entity.healthcare.Disease
import ffc.entity.healthcare.Icd10

internal interface Diseases {

    fun all(res: (List<Disease>, Throwable?) -> Unit)
}

internal fun diseases(context: Context): Diseases = MockDiseases(context)

private class MockDiseases(val context: Context) : Diseases {

    init {
        if (disease.isEmpty()) {
            disease = context.assetAs<List<Icd10>>("lookups/VisitIcd10.json")
        }
    }

    override fun all(res: (List<Disease>, Throwable?) -> Unit) {
        res(disease, null)
    }

    companion object {
        internal var disease: List<Disease> = listOf()
    }
}
