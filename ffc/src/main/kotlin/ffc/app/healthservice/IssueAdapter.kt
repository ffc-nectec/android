package ffc.app.healthservice

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ffc.android.getString
import ffc.android.inflate
import ffc.app.R
import ffc.app.util.datetime.toBuddistString
import ffc.entity.healthcare.analyze.HealthChecked
import ffc.entity.healthcare.analyze.HealthIssue
import ffc.entity.healthcare.analyze.HealthProblem
import kotlinx.android.synthetic.main.hs_issue_item_small.view.caption
import kotlinx.android.synthetic.main.hs_issue_item_small.view.icon
import kotlinx.android.synthetic.main.hs_issue_item_small.view.severity
import kotlinx.android.synthetic.main.hs_issue_item_small.view.title

class IssueAdapter(val issues: List<HealthIssue>) : RecyclerView.Adapter<IssueHolder>() {

    override fun onCreateViewHolder(view: ViewGroup, type: Int): IssueHolder = IssueHolder(view)

    override fun getItemCount() = issues.size

    override fun onBindViewHolder(holder: IssueHolder, position: Int) {
        holder.bind(issues[position])
    }
}

class IssueHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(parent.inflate(R.layout.hs_issue_item_small, false)) {

    fun bind(issue: HealthIssue) {
        with(itemView) {
            val pair: Pair<Int?, Int> = when (issue.issue) {
                HealthIssue.Issue.HT -> R.drawable.ic_bloodpressure_color_24dp to R.string.ht
                HealthIssue.Issue.DM -> R.drawable.ic_icecream_color_24dp to R.string.dm
                HealthIssue.Issue.CVD -> R.drawable.ic_cvd_black_24dp to R.string.cvd
                HealthIssue.Issue.ACTIVITIES -> R.drawable.ic_elder_couple_color_24dp to R.string.adl
                else -> null to R.string.app_name
            }
            title.text = getString(pair.second)
            pair.first?.let { icon.setImageResource(it) }
            caption.text = issue.date.toBuddistString()
            when (issue) {
                is HealthProblem -> {
                    severity.severity = issue.severity
                }
                is HealthChecked -> {
                    severity.severity = HealthIssue.Severity.UNDEFINED
                }
            }
        }
    }

    fun Drawable.setColorFilter(context: Context, @ColorRes colorRes: Int) {
        setColorFilter(ContextCompat.getColor(context, colorRes), android.graphics.PorterDuff.Mode.SRC_IN);
    }
}

