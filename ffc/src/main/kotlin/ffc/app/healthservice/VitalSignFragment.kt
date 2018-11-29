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
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.gone
import ffc.android.visible
import ffc.app.R
import ffc.app.familyFolderActivity
import ffc.app.person.personId
import ffc.app.util.datetime.toBuddistString
import ffc.entity.healthcare.HealthCareService
import ffc.entity.healthcare.bloodPressureLevel
import kotlinx.android.synthetic.main.hs_vitalsign_fragment.emptyView
import kotlinx.android.synthetic.main.hs_vitalsign_fragment.recycleView
import kotlinx.android.synthetic.main.hs_vitalsign_fragment.timestamp

internal class VitalSignFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_vitalsign_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val personId = arguments!!.personId!!
        healthCareServicesOf(personId, familyFolderActivity.org!!.id).all {
            onFound {
                val service = it.sortedByDescending { it.time }.get(0)
                with(recycleView) {
                    layoutManager = GridLayoutManager(context!!, 3)
                    adapter = HealthValueAdapter(service.toValues())
                }
                timestamp.text = "ข้อมูลเมื่อ ${service.time.toBuddistString()}"

                emptyView.gone()
            }
            onNotFound { emptyView.visible() }
            onFail { emptyView.visible() }
        }
    }
}

private fun HealthCareService.toValues(): List<Value> {
    val values = mutableListOf<Value>()
    bloodPressureLevel?.let {
        val captionColor = when {
            it.isHigh -> "สูง" to R.color.red_500
            it.isPreHigh -> "เสี่ยง" to R.color.orange_500
            it.isNormal -> "ปกติ" to R.color.colorAccent
            it.isLow -> "ต่ำ" to R.color.blue_500
            else -> null
        }
        values.add(Value("SYS/DIA", "${it.bp.systolic.toInt()}/${it.bp.diastolic.toInt()}", captionColor?.first, colorRes = captionColor?.second))
    }
    bmi?.let {
        val captionColor = when {
            it.isOverweight -> "สูง" to R.color.red_500
            it.isObese -> "เสี่ยง" to R.color.orange_500
            it.isNormal -> "ปกติ" to R.color.colorAccent
            it.isUnderWeight -> "ต่ำ" to R.color.blue_500
            else -> null
        }
        values.add(Value("BMI", "${it.value}", captionColor?.first, colorRes = captionColor?.second))
    }
    pulseRate?.let { values.add(Value("ชีพจร", "${it.toInt()}")) }
    respiratoryRate?.let { values.add(Value("อัตราหายใจ", "${it.toInt()}")) }
    waist?.let { values.add(Value("รอบเอว", "$it")) }

    return values
}

