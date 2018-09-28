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
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.transition.Fade
import android.view.Menu
import android.view.MenuItem
import ffc.android.enter
import ffc.android.exit
import ffc.android.onClick
import ffc.android.sceneTransition
import ffc.android.setTransition
import ffc.api.FfcCentral
import ffc.app.auth.auth
import ffc.app.location.GeoMapsFragment
import ffc.app.location.PlaceService
import ffc.app.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.navView
import kotlinx.android.synthetic.main.activity_main_content.addLocationButton
import kotlinx.android.synthetic.main.activity_main_content.searchButton
import kotlinx.android.synthetic.main.activity_main_content.toolbar
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import retrofit2.dsl.enqueue
import retrofit2.dsl.isNotFound

class MainActivity : FamilyFolderActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTransition {
            exitTransition = Fade().exit()
            reenterTransition = Fade().enter()
        }

        searchButton.onClick {
            startActivity(intentFor<SearchActivity>(),
                sceneTransition(toolbar to getString(R.string.transition_appbar)))
        }

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.contentContainer, GeoMapsFragment(), "geomap")
            .commit()
    }

    override fun onResume() {
        super.onResume()
        checkHouseNoLocation()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                toast(R.string.under_construction)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_gallery -> {
                toast(R.string.under_construction)
            }
            R.id.nav_manage -> {
                toast(R.string.under_construction)
            }
            R.id.nav_share -> {
                toast(R.string.under_construction)
            }
            R.id.nav_send -> {
                toast(R.string.under_construction)
            }
            R.id.nav_logout -> {
                auth(this).clear()
                finish()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun checkHouseNoLocation() {
        val placeService = FfcCentral().service<PlaceService>()
        placeService.listHouseNoLocation(org!!.id).enqueue {
            onSuccess { addLocationButton.show() }
            onClientError {
                when {
                    isNotFound -> addLocationButton.hide()
                }
            }
        }
        if (isDev) {
            addLocationButton.show()
        }
    }
}
