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

package ffc.v3.location

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.Menu
import ffc.v3.BaseActivity
import ffc.v3.R
import org.jetbrains.anko.toast

class HouseNoLocationActivtiy : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_house_no_location)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.house_no_location, menu)
    val searchView = menu.findItem(R.id.action_search).getActionView() as SearchView
    searchView.setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        toast("Submit $query")
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        toast("search for $newText")
        return true
      }

    })
    return true
  }

}
