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

package ffc.app

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import ffc.android.onClick
import ffc.app.search.SearchActivity
import ffc.util.gone
import kotlinx.android.synthetic.main.activity_maps.addLocationButton
import kotlinx.android.synthetic.main.activity_maps.searchButton
import org.jetbrains.anko.startActivity

const val REQ_ADD_LOCATION = 10214

class MapsActivity : FamilyFolderActivity() {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        searchButton.onClick { startActivity<SearchActivity>() }

        addLocationButton.gone()
    }
}

