package ffc.app.location

//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ffc.app.R
import ffc.entity.place.House

class HouseAdapter(
    private val houses: List<House>,
    val limit: Int = Int.MAX_VALUE,
    private val onItemClick: (House) -> Unit
) : RecyclerView.Adapter<HouseViewHolder>() {

    var onEmptyHouse: (() -> Unit)? = null
    var filteredHouse: List<House> = ArrayList(houses)
    var filter: String? = null
        set(value) {
            field = value
            if (value != null)
                filteredHouse = houses.filter { it.no?.contains(value) ?: false }
            else
                filteredHouse = ArrayList(houses)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_house, parent, false)
        return HouseViewHolder(view, onItemClick)
    }

    override fun getItemCount(): Int {
        val size = filteredHouse.size
        if (size == 0)
            onEmptyHouse?.invoke()
        return filteredHouse.size.takeIf { it < limit } ?: limit
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        holder.bind(filteredHouse[position])
    }
}
