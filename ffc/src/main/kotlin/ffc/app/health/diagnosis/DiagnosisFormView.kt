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
import android.util.AttributeSet
import android.widget.LinearLayout
import ffc.android.check
import ffc.app.R
import ffc.entity.healthcare.Diagnosis
import ffc.entity.healthcare.Disease
import ffc.entity.healthcare.Icd10
import me.piruin.spinney.Spinney
import org.jetbrains.anko.find

class DiagnosisFormView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var dxTypeField: Spinney<Diagnosis.Type>
    private var diseaseField: Spinney<Disease>

    init {
        inflate(context, R.layout.hs_diagnosis_view, this)
        orientation = LinearLayout.VERTICAL

        dxTypeField = find(R.id.dxTypeField)
        dxTypeField.setItems(dxTypes)
        diseaseField = find(R.id.diseaseField)
        diseases(context).all { list, _ ->
            diseaseField.setItemPresenter { item, _ ->
                item as Icd10
                "${item.icd10} - ${item.name}"
            }
            diseaseField.setSearchableItem(list)
        }
    }

    var diagnosis: Diagnosis?
        get() {
            diseaseField.check {
                on { dxTypeField.selectedItem != null }
                that { selectedItem != null }
                message = "กรุณาระบุ"
            }
            dxTypeField.check {
                on { diseaseField.selectedItem != null }
                that { selectedItem != null }
                message = "กรุณาระบุ"
            }

            if (diseaseField.selectedItem != null && dxTypeField.selectedItem != null)
                return Diagnosis(diseaseField.selectedItem!!, dxTypeField.selectedItem!!)
            else
                return null
        }
        set(value) {
            dxTypeField.selectedItem = value?.dxType
            diseaseField.selectedItem = value?.disease
        }
}

private val dxTypes = listOf(
    Diagnosis.Type.PRINCIPLE_DX,
    Diagnosis.Type.COMPLICATION,
    Diagnosis.Type.CO_MORBIDITY,
    Diagnosis.Type.EXTERNAL_CAUSE,
    Diagnosis.Type.OTHER
)
