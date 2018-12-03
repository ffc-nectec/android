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
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import ffc.android.addVeriticalItemDivider
import ffc.android.observe
import ffc.android.sceneTransition
import ffc.android.viewModel
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.dev
import ffc.app.location.HouseActivity
import ffc.app.location.HouseAdapter
import ffc.app.person.PersonAdapter
import ffc.app.person.startPersonActivityOf
import ffc.app.util.alert.handle
import ffc.entity.Person
import ffc.entity.place.House
import kotlinx.android.synthetic.main.activity_search_result.contentSwitcher
import kotlinx.android.synthetic.main.activity_search_result.emptyView
import kotlinx.android.synthetic.main.activity_search_result.emptyView2
import kotlinx.android.synthetic.main.activity_search_result.searchResultView
import kotlinx.android.synthetic.main.activity_search_result.searchResultView2
import kotlinx.android.synthetic.main.activity_search_result.tabLayout
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor

class SearchResultActivity : FamilyFolderActivity() {

    var Intent.query: String?
        get() = getStringExtra(SearchManager.QUERY)
        set(value) {
            putExtra(SearchManager.QUERY, value)
        }

    private val personViewModel by lazy { viewModel<SimpleViewModel>() }

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
        tabLayout.addOnTabSelectedListener(onTabSelect)
    }

    private val onTabSelect = object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
        override fun onTabReselected(tab: TabLayout.Tab) {}

        override fun onTabUnselected(tab: TabLayout.Tab) {}

        override fun onTabSelected(tab: TabLayout.Tab) {
            when (tab.position) {
                0 -> contentSwitcher.showPrevious()
                1 -> contentSwitcher.showNext()
            }
        }
    }

    private fun observeViewModelData() {
        observe(personViewModel.persons) {
            if (it.isNullOrEmpty()) {
                bindAdapter(listOf())
                emptyView.showEmpty()
            } else {
                bindAdapter(it)
                emptyView.showContent()
            }
        }
        observe(personViewModel.pLoading) { if (it == true) emptyView.showLoading() }
        observe(personViewModel.pException) {
            it?.let {
                handle(it)
                emptyView.error(it).show()
            }
        }

        observe(personViewModel.houses) {
            if (it.isNullOrEmpty()) {
                emptyView2.showEmpty()
                bindHouseAdapter(listOf())
            } else {
                emptyView2.showContent()
                bindHouseAdapter(it)
            }
        }
        observe(personViewModel.hLoading) { if (it == true) emptyView2.showLoading() }
        observe(personViewModel.hException) {
            it?.let { throwable ->
                handle(throwable)
                emptyView2.error(throwable).show()
            }
        }
    }

    private fun bindHouseAdapter(it: List<House>) {
        with(searchResultView2) {
            layoutManager = LinearLayoutManager(context)
            addVeriticalItemDivider(dimen(R.dimen.content_start_horizontal_padding))
            adapter = HouseAdapter(it) {
                val intent = intentFor<HouseActivity>("houseId" to it.id)
                startActivity(intent, sceneTransition())
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
            personViewModel.pLoading.value = true
            personSearcher(org!!.id).search(query!!) {
                always { personViewModel.pLoading.value = false }
                onFound { personViewModel.persons.value = it }
                onNotFound { personViewModel.persons.value = listOf() }
                onFail { personViewModel.pException.value = it }
            }

            personViewModel.hLoading.value = true
            houseSearcher(org!!.id).search(query!!) {
                always { personViewModel.hLoading.value = false }
                onFound { personViewModel.houses.value = it }
                onNotFound { personViewModel.houses.value = listOf() }
                onFail { personViewModel.hException.value = it }
            }
        } else {
            personViewModel.pException.value = IllegalArgumentException("ไม่มีคำค้นหา")
            personViewModel.hException.value = IllegalArgumentException("ไม่มีคำค้นหา")
        }
    }

    private fun bindAdapter(persons: List<Person>) {
        searchResultView.layoutManager = LinearLayoutManager(this)
        searchResultView.addVeriticalItemDivider(dimen(R.dimen.content_start_horizontal_padding))
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

    class SimpleViewModel : ViewModel() {
        val persons = MutableLiveData<List<Person>>()
        val pLoading = MutableLiveData<Boolean>()
        val pException = MutableLiveData<Throwable>()

        val houses = MutableLiveData<List<House>>()
        val hLoading = MutableLiveData<Boolean>()
        val hException = MutableLiveData<Throwable>()
    }
}
