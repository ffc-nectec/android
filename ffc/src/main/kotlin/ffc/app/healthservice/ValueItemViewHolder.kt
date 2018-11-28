package ffc.app.healthservice

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ffc.android.inflate
import ffc.app.R
import kotlinx.android.synthetic.main.value_grid_item.view.captionView
import kotlinx.android.synthetic.main.value_grid_item.view.labelView
import kotlinx.android.synthetic.main.value_grid_item.view.valueView

class ValueItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.value_grid_item, false)) {

    fun bind(value: Value) {
        bind(value.label, value.value, value.caption, value.color, value.colorRes)
    }

    fun bind(label: String, value: String = "-", caption: String? = null, @ColorInt color: Int? = null, @ColorRes colorRes: Int? = null) {
        with(itemView) {
            labelView.text = label
            valueView.text = value
            captionView.text = caption
            captionView.visibility = if (caption == null) View.INVISIBLE else View.VISIBLE
            color?.let { valueView.setTextColor(it) }
            colorRes?.let { valueView.setTextColor(ContextCompat.getColor(context, it)) }
        }
    }
}
