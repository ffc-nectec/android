package ffc.app.healthservice.analyze

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.app.R
import ffc.app.familyFolderActivity
import ffc.app.util.alert.handle
import ffc.entity.Person
import kotlinx.android.synthetic.main.hs_issue_fragment.recycleView
import org.jetbrains.anko.support.v4.toast

class HealthIssueFragment : Fragment() {

    var person: Person? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_issue_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recycleView) {
            layoutManager = LinearLayoutManager(context!!)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        person?.let {
            healthIssues().issueOf(it) {
                onFound {
                    recycleView.adapter = HealthIssueAdapter(it.toList())
                }
                onNotFound {
                    toast("Not found analyze result")
                }
                onFail {
                    familyFolderActivity.handle(it)
                }
            }
        }
    }
}
