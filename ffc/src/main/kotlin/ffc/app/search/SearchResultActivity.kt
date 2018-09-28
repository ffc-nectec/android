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
import android.support.v7.widget.LinearLayoutManager
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.dev
import ffc.app.person.PersonActvitiy
import ffc.app.person.PersonAdapter
import ffc.entity.Person
import kotlinx.android.synthetic.main.activity_search_result.searchResultView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SearchResultActivity : FamilyFolderActivity() {

    var Intent.query: String?
        get() = getStringExtra(SearchManager.QUERY)
        set(value) {
            putExtra(SearchManager.QUERY, value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        dev {
            if (intent.query == null) {
                //TODO remove this after developed
                intent.action = Intent.ACTION_SEARCH
                intent.query = "พิรุณ"
            }
        }

        with(supportActionBar!!) {
            title = intent.query
            setDisplayHomeAsUpEnabled(true)
            onToolbarClick { onBackPressed() }
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val query = intent.query
        if (Intent.ACTION_SEARCH == intent.action && intent.query != null) {
            supportActionBar!!.title = query
            personSearcher(org!!.id).search(query!!) {
                onFound { bindAdapter(persons = it) }
                onNotFound { bindAdapter(listOf())}
                onFail { toast("${it.message}") }
            }
        }
    }

    private fun bindAdapter(persons: List<Person>) {
        searchResultView.layoutManager = LinearLayoutManager(this)
        searchResultView.adapter = PersonAdapter(persons) {
            onItemClick { p -> startActivity<PersonActvitiy>("personId" to p.id) }
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
