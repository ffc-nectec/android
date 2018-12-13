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

package ffc.app.health.service.community

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.check
import ffc.android.observe
import ffc.android.viewModel
import ffc.app.R
import ffc.app.health.service.HealthCareServivceForm
import ffc.app.util.SimpleViewModel
import ffc.app.util.datetime.th_TH
import ffc.app.util.datetime.toCalendar
import ffc.app.util.datetime.toLocalDate
import ffc.app.util.setInto
import ffc.entity.healthcare.CommunityService
import ffc.entity.healthcare.HealthCareService
import ffc.entity.healthcare.HomeVisit
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.appointField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.detailField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.planField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.resultField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.syntomField
import me.piruin.spinney.Spinney
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast

internal class HomeVisitFormFragment : Fragment(), HealthCareServivceForm<HealthCareService> {

    val communityServicesField by lazy { find<Spinney<CommunityService.ServiceType>>(R.id.communityServiceField) }
    val serviceTypeViewModel by lazy { viewModel<ServicesTypeViewModel>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_homevisit_from_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        communityServicesField.setItemPresenter { item, _ -> "${item.id} - ${item.name}" }
        communityServicesField.setItems(listOf())
        if (appointField.tag == null) {
            appointField.setUndefinedAsDefault()
        }

        observe(serviceTypeViewModel.content) {
            if (!it.isNullOrEmpty()) {
                communityServicesField.setSearchableItem(it)
                serviceTypeViewModel.bindItem.value?.let { communityServicesField.selectedItem = it }
            }
        }
        observe(serviceTypeViewModel.exception) { toast(it?.message ?: "What happend") }
        observe(serviceTypeViewModel.bindItem) {
            if (!serviceTypeViewModel.content.value.isNullOrEmpty()) {
                communityServicesField.selectedItem = it
            }
        }

        communityServiceTypes(context!!).all {
            onFound { serviceTypeViewModel.content.value = it }
            onNotFound { serviceTypeViewModel.content.value = listOf() }
            onFail { serviceTypeViewModel.exception.value = it }
        }
    }

    override fun bind(service: HealthCareService) {
        service.communityServices.firstOrNull { it is HomeVisit }?.let {
            it as HomeVisit
            serviceTypeViewModel.bindItem.value = it.serviceType
            it.detail.setInto(detailField)
            it.result.setInto(resultField)
            it.plan.setInto(planField)
        }
        service.syntom.setInto(syntomField)
        service.nextAppoint?.let {
            appointField.tag = it
            appointField.calendar = it.toCalendar()
        }
    }

    override fun dataInto(services: HealthCareService) {
        communityServicesField.check {
            that { selectedItem != null }
            message = "กรุณาระบุประเภทการเยี่ยม"
        }
        appointField.check {
            on { calendar != null }
            that { calendar.after(services.time.toCalendar(th_TH)) }
            message = "นัดครั้งต่อไป ต้องหลังจากวันที่ให้บริการ"
        }

        services.apply {
            communityServices = mutableListOf(HomeVisit(communityServicesField.selectedItem!!,
                detail = detailField.text.toString(),
                result = resultField.text.toString(),
                plan = planField.text.toString()
            ))
            syntom = syntomField.text.toString()
            nextAppoint = appointField.calendar?.toLocalDate()
        }
    }

    class ServicesTypeViewModel : SimpleViewModel<List<CommunityService.ServiceType>>() {
        val bindItem = MutableLiveData<CommunityService.ServiceType>()
    }
}
