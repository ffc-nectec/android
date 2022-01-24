package ffc.app.location

//import android.support.v7.widget.RecyclerView
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ffc.entity.place.House
import kotlinx.android.synthetic.main.item_house.view.houseNo

class HouseViewHolder(view: View, val onItemClick: (House) -> Unit) : RecyclerView.ViewHolder(view) {
    fun bind(address: House) {
        with(address) {
            if(no!==null && villageName!=null) {
                itemView.houseNo.text = StringBuilder(no!!).apply { villageName?.let { append(" · $it") } }
                itemView.setOnClickListener { onItemClick(this) }
            }
        }
    }
}
