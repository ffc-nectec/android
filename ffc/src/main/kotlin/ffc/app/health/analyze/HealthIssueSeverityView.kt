/*
 * Copyright (c) 2019 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ffc.app.health.analyze

import android.content.Context
import android.graphics.drawable.ColorDrawable
//import android.support.annotation.ColorInt
//import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ffc.android.gone
import ffc.app.R
import ffc.entity.healthcare.Severity
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
                    views.filter {
                        listOf(R.id.severityLow, R.id.severityHi, R.id.severityVeryHi).contains(it.id)
                    }.forEach { it.gone() }
                    severity = if (value.haveIssue) Severity.MID else Severity.OK
                }
            }
        }

    var severity: Severity = Severity.UNDEFINED
        private set(value) {
            field = value
            tintAll()
            val mappedId: Int = when (value) {
                Severity.OK -> R.id.severityOk
                Severity.LOW -> R.id.severityLow
                Severity.UNDEFINED -> R.id.severityMid
                Severity.MID -> R.id.severityMid
                Severity.HI -> R.id.severityHi
                Severity.VERY_HI -> R.id.severityVeryHi
            }
            val highlightView = views.first { it.id == mappedId }
            highlightView.setImageDrawable(null)
        }

    private fun tintAll() {
        views.forEach { it.setImageDrawable(ColorDrawable(tint)) }
    }
}
