package ffc.app.health.analyze

import android.content.Context
import android.graphics.drawable.Drawable
//import android.support.annotation.ColorRes
//import android.support.v4.content.ContextCompat
//import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ffc.android.getString
import ffc.android.inflate
import ffc.app.R
import ffc.app.util.datetime.toBuddistString
import ffc.entity.healthcare.analyze.HealthIssue
import kotlinx.android.synthetic.main.hs_issue_item_small.view.caption
import kotlinx.android.synthetic.main.hs_issue_item_small.view.icon
import kotlinx.android.synthetic.main.hs_issue_item_small.view.severity
import kotlinx.android.synthetic.main.hs_issue_item_small.view.title

class HealthIssueAdapter(
    val issues: List<HealthIssue>,
    val limit: Int = Int.MAX_VALUE
) : RecyclerView.Adapter<IssueHolder>() {

    override fun onCreateViewHolder(view: ViewGroup, type: Int): IssueHolder = IssueHolder(view)

    override fun getItemCount() = issues.size.takeIf { it < limit } ?: limit

    override fun onBindViewHolder(holder: IssueHolder, position: Int) {
        holder.bind(issues[position])
    }
}

class IssueHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(parent.inflate(R.layout.hs_issue_item_small, false)) {

    fun bind(issue: HealthIssue) {
        with(itemView) {
            val pair = issue.issue.toIconTitlePair() ?: null to R.string.app_name
            title.text = if (pair.second != R.string.app_name) context.getString(pair.second) else issue.issue.name
            pair.first?.let { icon.setImageResource(it) }
            caption.text = issue.date.toBuddistString()
            severity.issue = issue
        }
    }

    fun Drawable.setColorFilter(context: Context, @ColorRes colorRes: Int) {
        setColorFilter(ContextCompat.getColor(context, colorRes), android.graphics.PorterDuff.Mode.SRC_IN)
    }
}

fun HealthIssue.Issue.toIconTitlePair() = when (this) {
    HealthIssue.Issue.HT -> R.drawable.ic_bloodpressure_color_24dp to R.string.ht
    HealthIssue.Issue.DM -> R.drawable.ic_icecream_color_24dp to R.string.dm
    HealthIssue.Issue.CVD -> R.drawable.ic_heart_cvd_color_24dp to R.string.cvd
    HealthIssue.Issue.DEMENTIA -> R.drawable.ic_brain_dementia_color_24dp to R.string.dementia
    HealthIssue.Issue.DEPRESSIVE -> R.drawable.ic_depression_color_24dp to R.string.depression
    HealthIssue.Issue.OA_KNEE -> R.drawable.ic_knee_black_24dp to R.string.oa_knee
    HealthIssue.Issue.FARSIGHTED -> R.drawable.ic_binocular_color_24dp to R.string.farsighted
    HealthIssue.Issue.NEARSIGHTED -> R.drawable.ic_eye_glasses_color_24dp to R.string.nearsighted
    HealthIssue.Issue.CATARACT -> R.drawable.ic_eye_cataract_color_24dp to R.string.cataract
    HealthIssue.Issue.GLAUCOMA -> R.drawable.ic_eye_glaucoma_color_24dp to R.string.glaucoma
    HealthIssue.Issue.AMD -> R.drawable.ic_eye_amd_color_24dp to R.string.amd
    HealthIssue.Issue.FALL_RISK -> R.drawable.ic_person_fall_black_24dp to R.string.fall_risk
    HealthIssue.Issue.ACTIVITIES -> R.drawable.ic_elder_couple_color_24dp to R.string.adl
    else -> null
}
