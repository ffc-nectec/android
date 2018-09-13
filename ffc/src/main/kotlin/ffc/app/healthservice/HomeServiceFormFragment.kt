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
import ffc.app.R
import ffc.entity.healthcare.CommunityServiceType
import ffc.entity.healthcare.HomeVisit
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.detailField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.planField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.resultField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.syntomField
import me.piruin.spinney.Spinney
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast

internal class HomeServiceFormFragment : Fragment() {

    val communityServicesField by lazy { find<Spinney<CommunityServiceType>>(R.id.communityServiceField) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_homevisit_from_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeVisitType(context!!).all { list, throwable ->
            if (throwable != null) {
                toast(throwable.message ?: "What happend")
            } else {
                communityServicesField.setItemPresenter { item, position ->
                    if (item is CommunityServiceType) {
                        "${item.id} - ${item.name}"
                    } else {
                        item.toString()
                    }
                }
                communityServicesField.setSearchableItem(list)
            }
        }
    }

    fun dataInto(visit: HomeVisit) {
        check(communityServicesField.selectedItem != null) { "กรุณาระบุ" }

        visit.apply {
            serviceType = communityServicesField.selectedItem!!
            syntom = syntomField.text.toString()
            detail = detailField.text.toString()
            result = resultField.text.toString()
            plan = planField.text.toString()
        }
    }
}
