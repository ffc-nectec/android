package ffc.app.healthservice

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ffc.android.inflate
import ffc.app.R

class HealthValueAdapter(
    val values: List<Value>,
    val style: Style = Style.GRID,
    val showOnlyFistLabel: Boolean = false
) : RecyclerView.Adapter<ValueItemViewHolder>() {

    enum class Style(@LayoutRes val layoutRes: Int) {
        GRID(R.layout.value_grid_item),
        SMALL(R.layout.value_small_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ValueItemViewHolder {
        return ValueItemViewHolder(parent.inflate(style.layoutRes, false))
    }

    override fun getItemCount() = values.size

    var lastLabel: String? = null

    override fun onBindViewHolder(holder: ValueItemViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
        if (showOnlyFistLabel && item.label == lastLabel)
            holder.hindLabel()
        lastLabel = item.label
    }
}

class Value(
    val label: String? = null,
    val value: String = "-",
    val caption: String? = null,
    @ColorInt val color: Int? = null,
    @ColorRes val colorRes: Int? = null
)
