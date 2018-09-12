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

package ffc.v3.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import ffc.entity.gson.toJson
import ffc.v3.BaseActivity
import ffc.v3.R
import ffc.v3.person.personSearcher
import org.jetbrains.anko.toast

class SearchResultActivity : BaseActivity() {

    val query by lazy { intent.getStringExtra(SearchManager.QUERY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        with(supportActionBar!!) {
            title = query
            setDisplayHomeAsUpEnabled(true)
            onToolbarClick { onBackPressed() }
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        toast("action = ${intent.action}")
        if (Intent.ACTION_SEARCH == intent.action) {
            supportActionBar!!.title = query
            toast("query $query")
            personSearcher().search(query) {
                always { toast("always") }
                onFound { toast(it.toJson()) }
                onNotFound { toast("Not found ") }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
