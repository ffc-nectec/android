package ffc.app.setting

import android.os.Bundle
import ffc.android.observe
import ffc.android.viewModel
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.util.SimpleViewModel
import kotlinx.android.synthetic.main.legal_view_activity.content
import kotlinx.android.synthetic.main.legal_view_activity.progress
import java.net.URL
import kotlin.concurrent.thread

class LegalViewActivity : FamilyFolderActivity() {

    private val viewModel by lazy { viewModel<SimpleViewModel<String>>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.legal_view_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observe(viewModel.content) { docContent ->
            docContent?.let {
                content.markdown = it
                viewModel.loading.value = false
            }
        }
        observe(viewModel.loading) {
            if (it == true) progress.show() else progress.hide()
        }

        viewModel.loading.value = true
        thread {
            URL(intent.data.toString()).openStream().reader().use { it.readText() }.let {
                runOnUiThread { viewModel.content.value = it.trim() }
            }
        }
    }
}
