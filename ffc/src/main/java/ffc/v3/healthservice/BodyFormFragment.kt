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
import ffc.entity.healthcare.HealthCareService
import ffc.v3.R
import kotlinx.android.synthetic.main.hs_body_form_fragment.heightField
import kotlinx.android.synthetic.main.hs_body_form_fragment.waistField
import kotlinx.android.synthetic.main.hs_body_form_fragment.weightField

class BodyFormFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_body_form_fragment, container, false)
    }

    fun dataInto(service: HealthCareService) {
        service.apply {
            heightField.text.let { height = it.toString().toDouble() }
            weightField.text.let { weight = it.toString().toDouble() }
            waistField.text.let {
                //TODO save waist on new version of entiity release
            }
        }
    }
}
