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

package ffc.app.health

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.check
import ffc.android.isNotBlank
import ffc.android.notEmpty
import ffc.app.R
import ffc.app.health.service.HealthCareServivceForm
import ffc.app.util.setInto
import ffc.entity.healthcare.BloodPressure
import ffc.entity.healthcare.HealthCareService
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.bpDiaField
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.bpDiaField2
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.bpSysField
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.bpSysField2
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.pulseField
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.rrField
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.tempField

internal class VitalSignFormFragment : Fragment(), HealthCareServivceForm<HealthCareService> {

    override fun bind(service: HealthCareService) {
        with(service) {
            bloodPressure?.let {
                it.systolic.setInto(bpSysField)
                it.diastolic.setInto(bpDiaField)
            }
            bloodPressure2nd?.let {
                it.systolic.setInto(bpSysField)
                it.diastolic.setInto(bpDiaField)
            }
            pulseRate.setInto(pulseField)
            respiratoryRate.setInto(rrField)
            bodyTemperature.setInto(tempField)
        }
    }

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
            it.bloodPressure2nd?.let { bp ->
                bpSysField2.setText("${bp.systolic}")
                bpDiaField2.setText("${bp.diastolic}")
            }
            it.respiratoryRate?.let { rr -> rrField.setText("$rr") }
            it.pulseRate?.let { pr -> pulseField.setText("$pr") }
            it.bodyTemperature?.let { temp -> tempField.setText("$temp") }
        }
    }

    override fun dataInto(service: HealthCareService) {
        bpSysField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 80..330 }
            message = "ค่าต้องอยู่ระหว่าง 80-330"
        }
        bpSysField.check {
            on { bpDiaField.isNotBlank }
            that { isNotBlank }
            message = "กรุณาระบุ"
        }
        bpDiaField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 30..135 }
            message = "ค่าต้องอยู่ระหว่าง 30-135"
        }
        bpDiaField.check {
            on { bpSysField.isNotBlank }
            that { isNotBlank }
            message = "กรุณาระบุ"
        }
        bpSysField2.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 80..330 }
            message = "ค่าต้องอยู่ระหว่าง 80-330"
        }
        bpSysField2.check {
            on { bpDiaField2.isNotBlank }
            that { isNotBlank }
            message = "กรุณาระบุ"
        }
        bpDiaField2.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 30..135 }
            message = "ค่าต้องอยู่ระหว่าง 30-135"
        }
        bpDiaField2.check {
            on { bpSysField2.isNotBlank }
            that { isNotBlank }
            message = "กรุณาระบุ"
        }
        pulseField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 30..250 }
            message = "ค่าต้องอยู่ระหว่าง 30-250"
        }
        rrField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 5..40 }
            message = "ค่าต้องอยู่ระหว่าง 5-40"
        }
        tempField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 35..45 }
            message = "ค่าต้องอยู่ระหว่าง 35-45"
        }

        service.apply {
            if (notEmpty(bpSysField, bpDiaField))
                bloodPressure = BloodPressure(
                    bpSysField.text.toString().toDouble(),
                    bpDiaField.text.toString().toDouble())
            if (notEmpty(bpSysField2, bpDiaField2))
                bloodPressure2nd = BloodPressure(
                    bpSysField2.text.toString().toDouble(),
                    bpDiaField2.text.toString().toDouble())
            if (notEmpty(rrField))
                respiratoryRate = rrField.text.toString().toDouble()
            if (notEmpty(pulseField))
                pulseRate = pulseField.text.toString().toDouble()
            if (notEmpty(tempField))
                bodyTemperature = tempField.text.toString().toDouble()
        }
    }
}
