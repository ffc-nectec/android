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

import android.os.Bundle
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ffc.android.onClick
import ffc.app.R
import ffc.app.health.service.HealthCareServivceForm
import ffc.entity.healthcare.Diagnosis
import ffc.entity.healthcare.HealthCareService
import kotlinx.android.synthetic.main.hs_diagnosis_fragment.addDiagnosis
import kotlinx.android.synthetic.main.hs_diagnosis_fragment.container
import org.jetbrains.anko.forEachChild

class DiagnosisFormFragment : Fragment(), HealthCareServivceForm<HealthCareService> {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_diagnosis_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addDiagnosis.onClick {
            container.addView(DiagnosisFormView(requireActivity()))
        }
    }

    override fun bind(service: HealthCareService) {
        service.diagnosises.forEach {
            container.addView(DiagnosisFormView(requireActivity()).apply { diagnosis = it })
        }
    }

    fun addDefaultPrincipleDx() {
        diseases(requireContext()).disease("Z71.8") {
            onFound { z718 ->
                container.addView(DiagnosisFormView(requireActivity()).apply {
                    diagnosis = Diagnosis(z718, Diagnosis.Type.PRINCIPLE_DX)
                })
            }
            onNotFound { } //do nothing
            onFail { } // do nothing
        }
    }

    override fun dataInto(service: HealthCareService) {
        val diag = container.diagView
            .filter { it.diagnosis != null }
            .map { it.diagnosis!! }
            .toMutableList()

        check(diag.isNotEmpty()) { "กรุณาระบุผลการวินิจฉัย" }
        check(diag.firstOrNull { it.dxType == Diagnosis.Type.PRINCIPLE_DX } != null) { "ต้องมี Principle Dx" }
        check(diag.filter { it.dxType == Diagnosis.Type.PRINCIPLE_DX }.size == 1) {
            "มี Principle Dx ได้แค่รายการเดียว"
        }

        service.apply {
            diagnosises = diag
        }
    }

    val ViewGroup.diagView: List<DiagnosisFormView>
        get() {
            val diagView = mutableListOf<DiagnosisFormView>()
            forEachChild {
                if (it is DiagnosisFormView) diagView.add(it)
            }
            return diagView
        }
}
