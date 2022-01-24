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

//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import android.os.Bundle
//import android.support.v7.widget.LinearLayoutManager
//import androidx.appcompat.widget.SearchView
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import ffc.android.addVeriticalItemDivider
import ffc.android.enter
import ffc.android.excludeSystemView
import ffc.android.exit
import ffc.android.gone
import ffc.android.observe
import ffc.android.searchManager
import ffc.android.setTransition
import ffc.android.viewModel
import ffc.android.visible
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.person.PersonAdapter
import ffc.app.person.startPersonActivityOf
import ffc.app.util.value.Value
import ffc.app.util.value.ValueAdapter
import ffc.entity.Person
import kotlinx.android.synthetic.main.search_activity.recentPerson
import kotlinx.android.synthetic.main.search_activity.recentPersonCard
import kotlinx.android.synthetic.main.search_activity.recentQuery
import kotlinx.android.synthetic.main.search_activity.recentQueryCard
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find

class SearchActivity : FamilyFolderActivity() {

    val viewModel by lazy { viewModel<SearchViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setTransition {
            enterTransition = Slide(Gravity.BOTTOM).excludeSystemView().enter()
            exitTransition = Fade().excludeSystemView().exit()
        }

        val searchView = find<SearchView>(R.id.searchView)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)

        observe(viewModel.recentQuery) { recent ->
            if (!recent.isNullOrEmpty()) {
                val values = recent.map { Value(value = it, iconRes = R.drawable.ic_search_black_24dp) }
                with(recentQuery) {
                    layoutManager = LinearLayoutManager(context)
                    addVeriticalItemDivider(dimen(R.dimen.content_start_horizontal_padding))
                    adapter = ValueAdapter(values, ValueAdapter.Style.NORMAL, limit = 4) {
                        onItemClick { v -> searchView.setQuery(v.value, true) }
                    }
                }
                recentQueryCard.visible()
            } else {
                recentQueryCard.gone()
            }
        }
        observe(viewModel.recentPerson) {
            if (!it.isNullOrEmpty()) {
                with(recentPerson) {
                    layoutManager = LinearLayoutManager(context)
                    addVeriticalItemDivider(dimen(R.dimen.content_start_horizontal_padding))
                    adapter = PersonAdapter(it, limit = 4) {
                        onItemClick { p ->
                            startPersonActivityOf(p, null,
                                find<ImageView>(R.id.personImageView) to getString(R.string.transition_person_profile))
                        }
                    }
                }
                recentPersonCard.visible()
            } else {
                recentPersonCard.gone()
            }
        }

        viewModel.recentQuery.value = RecentSearchProvider.query(this)
        viewModel.recentPerson.value = RecentPersonProvider(this, org!!).getRecentPerson()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    class SearchViewModel : ViewModel() {
        val recentQuery = MutableLiveData<List<String>>()
        val recentPerson = MutableLiveData<List<Person>>()
    }
}
