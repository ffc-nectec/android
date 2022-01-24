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

package ffc.app.auth.legal

import android.os.Bundle
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import retrofit2.Call
import retrofit2.dsl.enqueue

class LegalAgreementFragment : Fragment() {

    var legalDocCall: Call<String>? = null
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
        observe(viewModel.exception) {
            it?.let { viewModel.content.value = it.message }
        }

        viewModel.loading.value = true

        legalDocCall!!.enqueue {
            onSuccess { viewModel.content.value = body()!! }
            onError { viewModel.content.value = errorBody<String>()!! }
            onFailure { viewModel.exception.value = it }
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
