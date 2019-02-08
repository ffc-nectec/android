package ffc.app.auth.legal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.observe
import ffc.android.onClick
import ffc.android.onScrolledToBottom
import ffc.android.viewModel
import ffc.app.R
import ffc.app.util.SimpleViewModel
import ffc.app.util.md5
import kotlinx.android.synthetic.main.legal_fragment.agreement
import kotlinx.android.synthetic.main.legal_fragment.content
import kotlinx.android.synthetic.main.legal_fragment.progress
import kotlinx.android.synthetic.main.legal_fragment.scrollView
import org.jetbrains.anko.support.v4.onUiThread
import java.net.URL
import kotlin.concurrent.thread

class LegalDocumentFragment : Fragment() {

    var url: String? = null
    var onAccept: ((version: String) -> Unit)? = null

    private val viewModel by lazy { viewModel<SimpleViewModel<String>>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.legal_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observe(viewModel.content) {
            it?.let { onDocumentLoaded(it) }
        }
        observe(viewModel.loading) {
            if (it == true) progress.show() else progress.hide()
        }

        viewModel.loading.value = true
        thread {
            URL(url).openStream().reader().use { it.readText() }.let {
                onUiThread { viewModel.content.value = it.trim() }
            }
        }
    }

    private fun onDocumentLoaded(it: String) {
        content.markdown = it
        scrollView.onScrolledToBottom {
            //this block also fire when fragment was replaced. So, We need to check view nullness
            //Note: removeOnScrollChangedListener at fragment's onStop() not help
            agreement?.let {
                it.isEnabled = true
                it.text = "เข้าใจและยอมรับ"
                it.onClick { onAccept!!.invoke(viewModel.content.value!!.md5()) }
            }
        }
        viewModel.loading.value = false
    }
}
