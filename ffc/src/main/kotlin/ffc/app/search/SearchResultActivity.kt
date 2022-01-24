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
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ffc.android.find
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.dev
import ffc.app.util.Analytics
import org.jetbrains.anko.bundleOf

class SearchResultActivity : FamilyFolderActivity() {

    var Intent.query: String?
        get() = getStringExtra(SearchManager.QUERY)
        set(value) {
            putExtra(SearchManager.QUERY, value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_result_activity)
        dev {
            if (intent.query == null) {
                intent.action = Intent.ACTION_SEARCH
                intent.query = "พิรุณ"
            }
        }

        with(supportActionBar!!) {
            title = intent.query
            setDisplayHomeAsUpEnabled(true)
            onToolbarClick {
                finish()
                overridePendingTransition(android.R.anim.fade_in, 0)
            }
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val query = intent.query
        if (Intent.ACTION_SEARCH == intent.action && query != null) {
            Analytics.instance?.search(term = query)
            SearchRecentSuggestions(this, RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE)
                .saveRecentQuery(query, null)
            supportActionBar!!.title = query

            supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, ResultFragment().apply {
                    arguments = bundleOf("query" to query)
                }, "result").commit()
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
}

class ResultFragment : Fragment() {

    val query: String
        get() = requireArguments().getString("query")!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_result_main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val personResult = childFragmentManager.find<PersonSearchResultFragment>(R.id.personResult)
        personResult.query = query
        val houseResult = childFragmentManager.find<HouseSearchResultFragment>(R.id.houseResult)
        houseResult.query = query
    }
}
