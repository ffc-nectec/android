package ffc.app.health.analyze

import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.widget.DividerItemDecoration
//import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ffc.android.observe
import ffc.android.viewModel
import ffc.app.R
import ffc.app.familyFolderActivity
import ffc.app.util.SimpleViewModel
import ffc.app.util.alert.handle
import ffc.entity.Person
import ffc.entity.healthcare.analyze.HealthIssue
import kotlinx.android.synthetic.main.hs_issue_fragment.emptyView
import kotlinx.android.synthetic.main.hs_issue_fragment.recycleView

class HealthIssueFragment : Fragment() {

    var person: Person? = null
    val viewModel by lazy { viewModel<SimpleViewModel<List<HealthIssue>>>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_issue_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recycleView) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        observe(viewModel.content) {
            if (!it.isNullOrEmpty()) {
                emptyView.showContent()
                recycleView.adapter = HealthIssueAdapter(it)
            } else {
                emptyView.showEmpty()
            }
        }
        observe(viewModel.loading) { if (it == true) emptyView.showLoading() }
        observe(viewModel.exception) {
            it?.let {
                emptyView.error(it).show()
                familyFolderActivity.handle(it)
            }
        }

        person?.let {
            viewModel.loading.value = true
            healthIssues().issueOf(it) {
                always { viewModel.loading.value = false }
                onFound { viewModel.content.value = it.toList() }
                onNotFound { viewModel.content.value = listOf() }
                onFail { viewModel.exception.value = it }
            }
        }
    }
}
