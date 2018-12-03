package ffc.app.healthservice.analyze

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import ffc.android.gone
import ffc.app.R
import ffc.entity.healthcare.analyze.HealthChecked
import ffc.entity.healthcare.analyze.HealthIssue
import ffc.entity.healthcare.analyze.HealthProblem
import org.jetbrains.anko.find

class HealthIssueSeverityView @JvmOverloads constructor(
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
            R.id.severityOk,
            R.id.severityLow,
            R.id.severityMid,
            R.id.severityHi,
            R.id.severityVeryHi
        ).map { find<ImageView>(it) }
        tint = ContextCompat.getColor(context, R.color.overlay_white)
    }

    var issue: HealthIssue? = null
        set(value) {
            when (value) {
                is HealthProblem -> {
                    severity = value.severity
                }
                is HealthChecked -> {
                    views.filter { listOf(R.id.severityLow, R.id.severityHi, R.id.severityVeryHi).contains(it.id) }.forEach { it.gone() }
                    severity = if (value.haveIssue) HealthIssue.Severity.MID else HealthIssue.Severity.OK
                }
            }
        }

    var severity: HealthIssue.Severity = HealthIssue.Severity.UNDEFINED
        private set(value) {
            field = value
            tintAll()
            val mappedId: Int = when (value) {
                HealthIssue.Severity.OK -> R.id.severityOk
                HealthIssue.Severity.LOW -> R.id.severityLow
                HealthIssue.Severity.UNDEFINED -> R.id.severityMid
                HealthIssue.Severity.MID -> R.id.severityMid
                HealthIssue.Severity.HI -> R.id.severityHi
                HealthIssue.Severity.VERY_HI -> R.id.severityVeryHi
            }
            val highlightView = views.first { it.id == mappedId }
            highlightView.setImageDrawable(null)
        }

    private fun tintAll() {
        views.forEach { it.setImageDrawable(ColorDrawable(tint)) }
    }
}


