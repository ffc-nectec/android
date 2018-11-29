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

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.observe
import ffc.android.onClick
import ffc.android.viewModel
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.familyFolderActivity
import ffc.app.person.personId
import ffc.app.util.AdapterClickListener
import ffc.app.util.alert.handle
import ffc.entity.healthcare.HealthCareService
import kotlinx.android.synthetic.main.hs_services_list_card.emptyView
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.toast

class HealthCareServicesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    lateinit var viewModel: ServicesViewModel

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
            }
        }
        observe(viewModel.loading) { loading ->
            if (loading == true) emptyView.showLoading()
        }
        loadHealthcareServices()
    }

    private fun loadHealthcareServices() {
        viewModel.loading.value = true

        healthCareServicesOf(arguments!!.personId!!, familyFolderActivity.org!!.id).all {
            always { viewModel.loading.value = false }
            onFound { viewModel.services.value = it }
            onNotFound { viewModel.services.value = emptyList() }
            onFail { (activity as FamilyFolderActivity).handle(it) }
        }
    }

    private fun bind(services: List<HealthCareService>) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = HealthCareServiceAdapter(services) {
            onItemClick { toast(R.string.under_construction) }
        }
    }

    class ServicesViewModel : ViewModel() {
        val services = MutableLiveData<List<HealthCareService>>()
        val loading = MutableLiveData<Boolean>()
    }
}

class HealthCareServiceAdapter(
    val services: List<HealthCareService>,
    val limit: Int = 10,
    onClickDsl: AdapterClickListener<HealthCareService>.() -> Unit
) : RecyclerView.Adapter<ServiceViewHolder>() {

    val listener = AdapterClickListener<HealthCareService>().apply(onClickDsl)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hs_service_item, parent, false)
        return ServiceViewHolder(view)
    }

    override fun getItemCount() = if (services.size > limit) limit else services.size

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position])
        holder.itemView.onClick {
            listener.onItemClick!!.invoke(holder.itemView, services[position])
        }
    }
}

