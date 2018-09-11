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

package ffc.v3.healthservice

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.entity.healthcare.BloodPressure
import ffc.entity.healthcare.HealthCareService
import ffc.v3.R
import ffc.v3.android.notEmpty
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.bpDiaField
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.bpSysField
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.pulseField
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.rrField
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.tempField

internal class VitalSignFormFragment : Fragment() {

    var service: HealthCareService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_vitalsign_form_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        service?.let {
            it.bloodPressure?.let { bp ->
                bpSysField.setText("${bp.systolic}")
                bpDiaField.setText("${bp.diastolic}")
            }
            it.respiratoryRate?.let { rr -> rrField.setText("$rr") }
            it.pulseRate?.let { pr -> pulseField.setText("$pr") }
            it.bodyTemperature?.let { temp -> tempField.setText("$temp") }
            //TODO Temperature
        }
    }

    fun dataInto(visit: HealthCareService) {
        visit.apply {
            if (notEmpty(bpSysField, bpDiaField))
                bloodPressure = BloodPressure(
                    bpSysField.text.toString().toDouble(),
                    bpDiaField.text.toString().toDouble())
            if (notEmpty(rrField))
                respiratoryRate = rrField.text.toString().toDouble()
            if (notEmpty(pulseField))
                pulseRate = pulseField.text.toString().toDouble()
            if (notEmpty(tempField))
                bodyTemperature = tempField.text.toString().toDouble()
        }
    }
}
