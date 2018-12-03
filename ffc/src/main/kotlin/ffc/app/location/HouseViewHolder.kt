package ffc.app.location

import android.support.v7.widget.RecyclerView
import android.view.View
import ffc.entity.place.House
import kotlinx.android.synthetic.main.item_house.view.houseNo

class HouseViewHolder(view: View, val onItemClick: (House) -> Unit) : RecyclerView.ViewHolder(view) {
    fun bind(address: House) {
        with(address) {
            itemView.houseNo.text = no
            itemView.setOnClickListener { onItemClick(this) }
        }
    }
}
