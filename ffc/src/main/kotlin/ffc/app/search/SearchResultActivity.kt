/*
 * Copyright (c) 2018 NECTEC
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

package ffc.app.search

import android.app.SearchManager
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import ffc.android.observe
import ffc.android.viewModel
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.dev
import ffc.app.person.PersonAdapter
import ffc.app.person.startPersonActivityOf
import ffc.app.util.alert.handle
import ffc.entity.Person
import kotlinx.android.synthetic.main.activity_search_result.emptyView
import kotlinx.android.synthetic.main.activity_search_result.searchResultView
import org.jetbrains.anko.find

class SearchResultActivity : FamilyFolderActivity() {

    var Intent.query: String?
        get() = getStringExtra(SearchManager.QUERY)
        set(value) {
            putExtra(SearchManager.QUERY, value)
        }

    val viewModel by lazy { viewModel<SearchResultViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        dev {
            if (intent.query == null) {
                intent.action = Intent.ACTION_SEARCH
                intent.query = "พิรุณ"
            }
        }
        observeViewModelData()

        with(supportActionBar!!) {
            title = intent.query
            setDisplayHomeAsUpEnabled(true)
            onToolbarClick { onBackPressed() }
        }
        handleIntent(intent)
    }

    private fun observeViewModelData() {
        observe(viewModel.persons) {
            if (it.isNullOrEmpty()) {
                bindAdapter(listOf())
                emptyView.empty().setEmptyText("\"${intent.query}\"").show()
            } else {
                bindAdapter(it)
                emptyView.showContent()
            }
        }
        observe(viewModel.loading) {
            if (it == true) emptyView.showLoading()
        }
        observe(viewModel.exception) {
            it?.let {
                handle(it)
                emptyView.error(it).show()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val query = intent.query
        if (Intent.ACTION_SEARCH == intent.action && intent.query != null) {
            SearchRecentSuggestions(this, RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE)
                .saveRecentQuery(query, null)
            supportActionBar!!.title = query
            viewModel.loading.value = false
            personSearcher(org!!.id).search(query!!) {
                always { viewModel.loading.value = false }
                onFound { viewModel.persons.value = it }
                onNotFound { viewModel.persons.value = listOf() }
                onFail { viewModel.exception.value = it }
            }
        } else {
            viewModel.exception.value = IllegalArgumentException("ไม่มีคำค้นหา")
        }
    }

    private fun bindAdapter(persons: List<Person>) {
        searchResultView.layoutManager = LinearLayoutManager(this)
        searchResultView.adapter = PersonAdapter(persons) {
            onItemClick { p ->
                startPersonActivityOf(p, null,
                    find<ImageView>(R.id.personImageView) to getString(R.string.transition_person_profile))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        overridePendingTransition(0, 0)
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, 0)
    }

    class SearchResultViewModel : ViewModel() {
        val persons = MutableLiveData<List<Person>>()
        val loading = MutableLiveData<Boolean>()
        val exception = MutableLiveData<Throwable>()
    }
}
