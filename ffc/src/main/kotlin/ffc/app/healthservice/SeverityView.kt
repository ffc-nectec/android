package ffc.app.healthservice

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import ffc.app.R
import ffc.entity.healthcare.analyze.HealthIssue
import org.jetbrains.anko.find

class SeverityView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val views: List<ImageView>
    @ColorInt
    val tint: Int

    init {
        inflate(context, R.layout.hs_severity_view, this)
        orientation = LinearLayout.HORIZONTAL
        views = listOf(
            R.id.severity1,
            R.id.severity2,
            R.id.severity3,
            R.id.severity4,
            R.id.severity5
        ).map { find<ImageView>(it) }
        tint = ContextCompat.getColor(context, R.color.overlay_white)
    }

    var severity: HealthIssue.Severity = HealthIssue.Severity.UNDEFINED
        set(value) {
            field = value
            tintAll()
            val mappedId: Int = when (value) {
                HealthIssue.Severity.UNDEFINED -> R.id.severity1
                HealthIssue.Severity.LOW -> R.id.severity2
                HealthIssue.Severity.MID -> R.id.severity3
                HealthIssue.Severity.HI -> R.id.severity4
                HealthIssue.Severity.VERY_HI -> R.id.severity5
                else -> R.id.severity1
            }
            val highlightView = views.first { it.id == mappedId }
            highlightView.setImageDrawable(null)
        }

    private fun tintAll() {
        views.forEach { it.setImageDrawable(ColorDrawable(tint)) }
    }
}


