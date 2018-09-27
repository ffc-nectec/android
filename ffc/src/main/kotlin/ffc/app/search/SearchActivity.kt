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

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import ffc.android.enter
import ffc.android.excludeSystemView
import ffc.android.exit
import ffc.android.searchManager
import ffc.android.setTransition
import ffc.app.FamilyFolderActivity
import ffc.app.R
import org.jetbrains.anko.find

class SearchActivity : FamilyFolderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setTransition {
            enterTransition = Slide(Gravity.BOTTOM).excludeSystemView().enter()
            exitTransition = Fade().excludeSystemView().exit()
        }

        val searchView = find<SearchView>(R.id.searchView)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
