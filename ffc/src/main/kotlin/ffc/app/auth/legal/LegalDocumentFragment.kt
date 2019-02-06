package ffc.app.auth.legal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.onClick
import ffc.app.R
import ffc.app.util.md5
import kotlinx.android.synthetic.main.legal_fragment.agreement
import kotlinx.android.synthetic.main.legal_fragment.content
import kotlinx.android.synthetic.main.legal_fragment.scrollView
import org.jetbrains.anko.support.v4.onUiThread
import java.net.URL
import kotlin.concurrent.thread

class LegalDocumentFragment : Fragment() {

    var url: String? = null
    var onAccept: ((version: String) -> Unit)? = null
    var docContent: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.legal_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        thread {
            URL(url).openStream().reader().use { it.readText() }.trim().let {
                onUiThread { onDocumentLoaded(it) }
            }
        }
    }

    private fun onDocumentLoaded(it: String) {
        docContent = it
        content.loadMarkdown(it)
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            if (scrollView.getChildAt(0).bottom <= (scrollView.height + scrollView.scrollY)) {
                //scroll view is at bottom
                onScrolledToBottom()
            } else {
                //scroll view is not at bottom
            }
        }
    }

    private fun onScrolledToBottom() {
        agreement.isEnabled = true
        agreement.onClick { onAccept!!.invoke(docContent!!.md5()) }
    }
}
