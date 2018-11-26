package ffc.app.healthservice

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.app.R
import ffc.entity.healthcare.analyze.HealthIssue
import kotlinx.android.synthetic.main.hs_issue_fragment.recycleView

class HealthIssueFragment : Fragment() {

    var issues: List<HealthIssue> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_issue_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recycleView) {
            layoutManager = LinearLayoutManager(context!!)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = IssueAdapter(issues)
        }
    }
}
