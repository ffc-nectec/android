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

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.felipecsl.asymmetricgridview.AsymmetricRecyclerView
import ffc.android.drawable
import ffc.android.getString
import ffc.android.gone
import ffc.android.onClick
import ffc.android.visible
import ffc.app.R
import ffc.app.familyFolderActivity
import ffc.app.person.personId
import ffc.app.photo.asymmetric.bind
import ffc.app.photo.bindPhotoUrl
import ffc.app.util.AdapterClickListener
import ffc.app.util.timeago.toTimeAgo
import ffc.entity.healthcare.HealthCareService
import ffc.entity.healthcare.HomeVisit
import kotlinx.android.synthetic.main.hs_services_list_card.emptyView
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.toast

class HealthCareServicesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.hs_services_list_card, container, false)
        recyclerView = view.find(R.id.recycleView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val personId = arguments!!.personId!!
        healthCareServicesOf(personId, familyFolderActivity.org!!.id).all {
            onFound {
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
                recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                recyclerView.adapter = HealthCareServiceAdapter(it) {
                    onItemClick {
                        toast(R.string.under_construction)
                    }
                }
                emptyView.gone()
            }
            onNotFound {
                emptyView.visible()
            }
        }
    }
}

class HealthCareServiceAdapter(
    val services: List<HealthCareService>,
    onClickDsl: AdapterClickListener<HealthCareService>.() -> Unit
) : RecyclerView.Adapter<ServiceViewHolder>() {

    val listener = AdapterClickListener<HealthCareService>().apply(onClickDsl)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hs_service_item, parent, false)
        return ServiceViewHolder(view)
    }

    override fun getItemCount() = services.size

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position])
        holder.itemView.onClick {
            listener.onItemClick!!.invoke(holder.itemView, services[position])
        }
    }
}

class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val icon = view.find<ImageView>(R.id.serviceIconView)
    val title = view.find<TextView>(R.id.serviceTitleView)
    val date = view.find<TextView>(R.id.serviceDateView)
    val dx = view.find<TextView>(R.id.serviceDxView)
    val caption = view.find<TextView>(R.id.captionView)
    val photos = view.find<AsymmetricRecyclerView>(R.id.photos)

    fun bind(services: HealthCareService) {
        with(services) {
            date.text = services.time.toTimeAgo()
            dx.text = services.principleDx!!.icd10
            when (services) {
                is HomeVisit -> {
                    title.text = getString(R.string.home_visit)
                    icon.setImageDrawable(itemView.context.drawable(R.drawable.ic_home_visit_color_24dp))
                    caption.text = (this as HomeVisit).serviceType.name
                    photos.bind(photosUrl)
                }
            }
        }
    }
}
