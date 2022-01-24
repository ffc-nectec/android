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

import android.annotation.SuppressLint
//import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
//import android.support.design.widget.TextInputEditText
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import ffc.android.check
import ffc.android.observe
import ffc.android.onLongClick
import ffc.android.viewModel
import ffc.app.R
import ffc.app.familyFolderActivity
import ffc.app.health.service.HealthCareServivceForm
import ffc.app.util.SimpleViewModel
import ffc.app.util.datetime.th_TH
import ffc.app.util.datetime.toCalendar
import ffc.app.util.datetime.toLocalDate
import ffc.app.util.setInto
import ffc.entity.Template
import ffc.entity.healthcare.CommunityService
import ffc.entity.healthcare.HealthCareService
import ffc.entity.healthcare.HomeVisit
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.appointField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.detailField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.planField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.resultField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.syntomField
import me.piruin.spinney.Spinney
import me.piruin.spinney.SpinneyAdapter
import me.piruin.spinney.SpinneyDialog
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast


internal class HomeVisitFormFragment : Fragment(), HealthCareServivceForm<HealthCareService> {

    val communityServicesField by lazy { find<Spinney<CommunityService.ServiceType>>(R.id.communityServiceField) }
    val viewModel by lazy { viewModel<ServicesTypeViewModel>() }

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

        observe(viewModel.content) {
            if (!it.isNullOrEmpty()) {
                communityServicesField.setSearchableItem(it)
                viewModel.bindItem.value?.let { communityServicesField.selectedItem = it }
            }
        }
        //observe(viewModel.exception) { toast(it?.message ?: "What happend") }
        observe(viewModel.bindItem) {
            if (!viewModel.content.value.isNullOrEmpty()) {
                communityServicesField.selectedItem = it
            }
        }
        observe(viewModel.templates) { templates ->
            if (!templates.isNullOrEmpty()) {
                syntomField.setTemplateAdapter(
                    templates.filter { it.field == "HealthCareService.syntom" }
                )
                detailField.setTemplateAdapter(
                    templates.filter { it.field == "HomeVisit.detail" }
                )
                resultField.setTemplateAdapter(
                    templates.filter { it.field == "HomeVisit.result" }
                )
            }
        }

        communityServiceTypes(requireContext()).all {
            onFound { viewModel.content.value = it }
            onNotFound { viewModel.content.value = listOf() }
            onFail { viewModel.exception.value = it }
        }

        templatesOf(familyFolderActivity.org!!).all {
            onFound { viewModel.templates.value = it }
            onNotFound { viewModel.templates.value = listOf() }
            onFail { viewModel.exception.value = it }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun TextInputEditText.setTemplateAdapter(template: List<Template>) {
        onLongClick { view ->
            val dialog = SpinneyDialog(view.context)
            dialog.setAdapter(SpinneyAdapter(view.context, template.map { it.value }))
            dialog.setOnItemSelectedListener { item, _ ->
                view.setText("${view.text ?: ""} ${item as String}".trim())
                true
            }
            dialog.show()
            true
        }
    }

    override fun bind(service: HealthCareService) {
        service.communityServices.firstOrNull { it is HomeVisit }?.let {
            it as HomeVisit
            viewModel.bindItem.value = it.serviceType
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
            that {selectedItem != null }
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

        val templates = MutableLiveData<List<Template>>()
    }
}
