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

package ffc.app.healthservice

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.getInput
import ffc.app.R
import ffc.entity.healthcare.HealthCareService
import kotlinx.android.synthetic.main.hs_body_form_fragment.*

class BodyFormFragment : Fragment(), HealthCareServivceForm<HealthCareService> {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_body_form_fragment, container, false)
    }

    override fun dataInto(service: HealthCareService) {
        service.apply {
            heightField.getInput { height = it.toDouble() }
            weightField.getInput { weight = it.toDouble() }
            waistField.getInput { waist = it.toDouble() }
            assField.getInput { ass = it.toDouble() }
        }
    }
}
