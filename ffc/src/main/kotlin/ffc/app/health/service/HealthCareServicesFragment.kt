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

package ffc.app.health.service

//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.widget.DividerItemDecoration
//import android.support.v7.widget.GridLayoutManager
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ffc.android.addHorizontalItemDivider
import ffc.android.observe
import ffc.android.viewModel
import ffc.app.R
import ffc.app.familyFolderActivity
import ffc.app.health.service.community.HomeVisitActivity
import ffc.app.person.personId
import ffc.app.util.alert.handle
import ffc.app.util.value.Value
import ffc.app.util.value.ValueAdapter
import ffc.entity.gson.toJson
import ffc.entity.healthcare.HealthCareService
import ffc.entity.healthcare.bloodPressureLevel
import kotlinx.android.synthetic.main.hs_services_list_card.emptyView
import kotlinx.android.synthetic.main.hs_services_list_card.vitalSign
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class HealthCareServicesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: ServicesViewModel

    private val personId: String?
        get() = requireArguments().personId

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.hs_services_list_card, container, false)
        recyclerView = view.find(R.id.recycleView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModel()
        observe(viewModel.services) {
            if (it == null || it.isEmpty())
                emptyView.showEmpty()
            else {
                emptyView.showContent()
                bind(it)
                bindVitalSign(it[0])
            }
        }
        observe(viewModel.loading) { loading ->
            if (loading == true && viewModel.services.value.isNullOrEmpty())
                emptyView.showLoading()
        }
        observe(viewModel.exception) {
            it?.let { t ->
                emptyView.error(t).show()
                familyFolderActivity.handle(t)
            }
        }
        loadHealthcareServices()
    }

    private fun bindVitalSign(service: HealthCareService) {
        with(vitalSign) {
            layoutManager = GridLayoutManager(requireContext(), 3)
            addHorizontalItemDivider()
            adapter = ValueAdapter(service.toValues(), limit =3)
        }
    }

    private fun loadHealthcareServices() {
        viewModel.loading.value = true

        healthCareServicesOf(personId!!, familyFolderActivity.org!!.id).all {
            always { viewModel.loading.value = false }
            onFound { viewModel.services.value = it.sortedByDescending { it.time } }
            onNotFound { viewModel.services.value = emptyList() }
            onFail { viewModel.exception.value = it }
        }
    }

    private fun bind(services: List<HealthCareService>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = HealthCareServiceAdapter(services) {
            onItemClick { toast(R.string.under_construction) }
            onViewClick { view, healthCareService ->
                startActivity<HomeVisitActivity>(
                    "personId" to personId,
                    "service" to healthCareService.toJson())
            }
        }
    }

    class ServicesViewModel : ViewModel() {
        val services = MutableLiveData<List<HealthCareService>>()
        val loading = MutableLiveData<Boolean>()
        val exception = MutableLiveData<Throwable>()
    }

    private fun HealthCareService.toValues(): List<Value> {
        val values = mutableListOf<Value>()
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
        bloodPressureLevel?.let {
            val captionColor = when {
                it.isHigh -> "สูง" to R.color.red_500
                it.isPreHigh -> "เสี่ยง" to R.color.orange_500
                it.isNormal -> "ปกติ" to R.color.colorAccent
                it.isLow -> "ต่ำ" to R.color.blue_500
                else -> null
            }
            values.add(Value("SYS/DIA", "${it.bp.systolic.toInt()}/${it.bp.diastolic.toInt()}",
                captionColor?.first,
                colorRes = captionColor?.second))
        }
        pulseRate?.let {
            val captionColor = when (it) {
                in 60.0..100.0 -> "ปกติ" to R.color.colorAccent
                else -> "ผิดปกติ" to R.color.orange_500
            }
            values.add(Value("Pulse", "${it.toInt()}", captionColor.first, colorRes = captionColor.second))
        }
        bodyTemperature?.let {
            val captionColor = when (it) {
                in 36.5..37.5 -> "ปกติ" to R.color.colorAccent
                else -> "ผิดปกติ" to R.color.orange_500
            }
            values.add(Value("Temp", "$it", captionColor.first, colorRes = captionColor.second))
        }
        respiratoryRate?.let {
            val captionColor = when (it) {
                in 16.0..20.0 -> "ปกติ" to R.color.colorAccent
                else -> "ผิดปกติ" to R.color.orange_500
            }
            values.add(Value("RR", "${it.toInt()}", captionColor.first, colorRes = captionColor.second))
        }
        waist?.let { values.add(Value("Waist", "$it")) }
        return values
    }
}
