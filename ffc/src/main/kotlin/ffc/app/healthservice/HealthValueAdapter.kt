package ffc.app.healthservice

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class HealthValueAdapter(val values: List<Value>) : RecyclerView.Adapter<ValueItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ValueItemViewHolder {
        return ValueItemViewHolder(parent)
    }

    override fun getItemCount() = values.size

    override fun onBindViewHolder(holder: ValueItemViewHolder, position: Int) {
        holder.bind(values[position])
    }
}

class Value(
    val label: String,
    val value: String = "-",
    val caption: String? = null,
    @ColorInt val color: Int? = null,
    @ColorRes val colorRes: Int? = null
)
