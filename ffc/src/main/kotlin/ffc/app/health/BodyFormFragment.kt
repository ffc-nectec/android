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
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ffc.android.check
import ffc.android.getInput
import ffc.android.isNotBlank
import ffc.app.R
import ffc.app.health.service.HealthCareServivceForm
import ffc.app.util.setInto
import ffc.entity.healthcare.HealthCareService
import ffc.entity.update
import kotlinx.android.synthetic.main.hs_body_form_fragment.heightField
import kotlinx.android.synthetic.main.hs_body_form_fragment.waistField
import kotlinx.android.synthetic.main.hs_body_form_fragment.weightField

class BodyFormFragment : Fragment(), HealthCareServivceForm<HealthCareService> {
    override fun bind(service: HealthCareService) {
        with(service) {
            height.setInto(heightField)
            weight.setInto(weightField)
            waist.setInto(waistField)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_body_form_fragment, container, false)
    }

    override fun dataInto(service: HealthCareService) {
        heightField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 10.0..250.0 }
            message = "ค่าต้องอยู่ระหว่าง 10-250"
        }
        weightField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 2.0..250.0 }
            message = "ค่าต้องอยู่ระหว่าง 2-250"
        }
        waistField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 15.0..500.0 }
            message = "ค่าต้องอยู่ระหว่าง 15-500"
        }

        service.update {
            heightField.getInput { height = it.toDouble() }
            weightField.getInput { weight = it.toDouble() }
            waistField.getInput { waist = it.toDouble() }
        }
    }
}
