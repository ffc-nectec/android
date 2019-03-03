package ffc.app.location

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ffc.android.color
import ffc.app.R
import org.jetbrains.anko.dip

class LegendView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = LinearLayout.HORIZONTAL
    }

    fun setLegend(vararg colorTextPair: Pair<Int, Int>) {
        removeAllViews()
        val layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f).apply {
            gravity = Gravity.CENTER_VERTICAL
        }
        colorTextPair.forEach {
            val item = LegendItem(context).apply {
                colorRes = it.first
                textRes = it.second
            }
            addView(item, layoutParams)
        }
    }
}

class LegendItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.AppTheme
) : LinearLayout(context, attrs, defStyleAttr) {

    val colorView = ImageView(context, attrs, defStyleAttr)
    val textView = TextView(context, attrs, defStyleAttr)

    var colorRes: Int = -1
        set(@ColorRes value) {
            field = value
            colorView.setBackgroundColor(color(value))
        }

    var textRes: Int = -1
        set(@StringRes value) {
            field = value
            textView.setText(value)
        }

    init {
        orientation = LinearLayout.VERTICAL

        colorView.setBackgroundColor(color(R.color.colorAccent))
        textView.apply {
            //textAppearance = android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Body1
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }

        addView(colorView, LayoutParams(LayoutParams.MATCH_PARENT, dip(12)))
        addView(textView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            topMargin = dip(4)
        })

        if (isInEditMode) {
            textRes = R.string.app_name
            colorRes = R.color.colorAccent
        }
    }
}

