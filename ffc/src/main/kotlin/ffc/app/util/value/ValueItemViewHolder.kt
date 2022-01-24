package ffc.app.util.value

//import android.support.annotation.ColorInt
//import android.support.annotation.ColorRes
//import android.support.annotation.DrawableRes
//import android.support.v4.content.ContextCompat
//import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import ffc.android.gone
import ffc.app.R
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional

class ValueItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val captionView: TextView
    private val labelView: TextView
    private val valueView: TextView
    private val iconView: ImageView?

    init {
        captionView = itemView.find(R.id.captionView)
        labelView = itemView.find(R.id.labelView)
        valueView = itemView.find(R.id.valueView)
        iconView = itemView.findOptional(R.id.iconView)
    }

    fun bind(value: Value) {
        bind(value.label, value.value, value.caption, value.color, value.colorRes, value.iconRes)
    }

    fun bind(
        label: String?,
        value: String = "-",
        caption: String? = null,
        @ColorInt color: Int? = null,
        @ColorRes colorRes: Int? = null,
        @DrawableRes iconRes: Int? = null
    ) {
        with(itemView) {
            labelView.text = label?.trim()
            labelView.visibility = if (label.isNullOrBlank()) View.GONE else View.VISIBLE
            valueView.text = value.trim()
            captionView.text = caption?.trim()
            captionView.visibility = if (caption == null) View.GONE else View.VISIBLE
            color?.let { valueView.setTextColor(it) }
            colorRes?.let { valueView.setTextColor(getColor(context, it)) }
            iconView?.let {
                it.setImageResource(iconRes ?: 0)
            }
        }
    }

    fun hindLabel() {
        labelView.gone()
    }
}
