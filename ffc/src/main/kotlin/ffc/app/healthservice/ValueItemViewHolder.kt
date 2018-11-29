package ffc.app.healthservice

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import ffc.android.gone
import ffc.app.R
import org.jetbrains.anko.find

class ValueItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val captionView: TextView
    private val labelView: TextView
    private val valueView: TextView

    init {
        captionView = itemView.find(R.id.captionView)
        labelView = itemView.find(R.id.labelView)
        valueView = itemView.find(R.id.valueView)
    }

    fun bind(value: Value) {
        bind(value.label, value.value, value.caption, value.color, value.colorRes)
    }

    fun bind(label: String?, value: String = "-", caption: String? = null, @ColorInt color: Int? = null, @ColorRes colorRes: Int? = null) {
        with(itemView) {
            labelView.text = label
            labelView.visibility = if (label.isNullOrBlank()) View.GONE else View.VISIBLE
            valueView.text = value
            captionView.text = caption
            captionView.visibility = if (caption == null) View.GONE else View.VISIBLE
            color?.let { valueView.setTextColor(it) }
            colorRes?.let { valueView.setTextColor(ContextCompat.getColor(context, it)) }
        }
    }

    fun hindLabel() {
        labelView.gone()
    }
}
