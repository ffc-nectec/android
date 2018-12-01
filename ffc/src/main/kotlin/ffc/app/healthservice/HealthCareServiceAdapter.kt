package ffc.app.healthservice

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ffc.android.onClick
import ffc.app.R
import ffc.app.util.AdapterClickListener
import ffc.entity.healthcare.HealthCareService

class HealthCareServiceAdapter(
    val services: List<HealthCareService>,
    val limit: Int = 10,
    onClickDsl: AdapterClickListener<HealthCareService>.() -> Unit
) : RecyclerView.Adapter<HealthCareServiceViewHolder>() {

    private val listener = AdapterClickListener<HealthCareService>().apply(onClickDsl)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): HealthCareServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hs_service_item, parent, false)
        return HealthCareServiceViewHolder(view)
    }

    override fun getItemCount() = if (services.size > limit) limit else services.size

    override fun onBindViewHolder(holder: HealthCareServiceViewHolder, position: Int) {
        holder.bind(services[position])
        holder.itemView.onClick {
            listener.onItemClick!!.invoke(holder.itemView, services[position])
        }
    }
}
