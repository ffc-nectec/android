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

//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
//import android.support.design.widget.NavigationView
//import android.support.v4.view.GravityCompat
//import android.support.v7.app.ActionBarDrawerToggle
//import android.support.v7.app.AlertDialog
import android.transition.Fade
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.berry_med.monitordemo.activity.DeviceMainActivity
import com.google.android.material.navigation.NavigationView
import ffc.android.enter
import ffc.android.load
import ffc.android.observe
import ffc.android.onClick
import ffc.android.sceneTransition
import ffc.android.setTransition
import ffc.android.viewModel
import ffc.app.asm.HomeListActivity
import ffc.app.auth.Users
import ffc.app.auth.auth
import ffc.app.location.GeoMapsFragment
import ffc.app.location.housesOf
import ffc.app.report.IncidentReportActivity
import ffc.app.report.ReportActivity
import ffc.app.search.SearchActivity
import ffc.app.setting.AboutActivity
import ffc.app.setting.SettingsActivity
import ffc.entity.User
import kotlinx.android.synthetic.main.activity_asm_menu.*
import kotlinx.android.synthetic.main.activity_asm_menu.view.*
import kotlinx.android.synthetic.main.activity_asm_menu.view.tvAsmName

import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.navView
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.activity_main_content.toolbar
import kotlinx.android.synthetic.main.activity_main_content.view.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
class MainActivity : FamilyFolderActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val geoMapsFragment by lazy { GeoMapsFragment() }

    private val viewModel by lazy { viewModel<MainViewModel>() }
    lateinit var asm:View
    lateinit var map:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = auth(applicationContext).user!!
        asm  = findViewById(R.id.asmMenu);
        map  = findViewById(R.id.map);
        if(user.roles[0]== User.Role.SURVEYOR){
            var info = user.displayName;
            asm.visibility = View.VISIBLE;
            if(user.tel!=null){
                info = info+"\nเบอร์โทรศัพท์:"+user.tel
            }
            else{
                info = info+"\nเบอร์โทรศัพท์:";
            }
            tvAsmName.setText(info)
            if(user.avatarUrl!=null) {
                asm.avatarView.load(Uri.parse(user.avatarUrl))
            }
            asm.homeAsUp.setOnClickListener{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("ยืนยัน")
                builder.setTitle("คุณต้องการออกจากระบบ หรือไม่")
                builder.setPositiveButton(android.R.string.yes){ dialog, which ->
                    auth(this).clear()
                    finish()
                }
                builder.setNegativeButton(android.R.string.no){ dialog, which ->
                }
                builder.show()

            }
            map.visibility = View.INVISIBLE;
            asm.btnLocation.setOnClickListener{
//                asm.visibility=View.INVISIBLE;
//                map.visibility = View.VISIBLE;
//                btnBack.show()
                startActivity<HomeListActivity>()
            }
            asm.btnReport.setOnClickListener{
                startActivity<IncidentReportActivity>()
            }
        }
        else if(user.roles[0]!=User.Role.SURVEYOR){
            asm.visibility = View.INVISIBLE;
            map.visibility = View.VISIBLE;
            btnBack.hide()
        }
        setTransition {
            exitTransition = null
            reenterTransition = Fade().enter()
        }

        searchButton.onClick {
            startActivity(intentFor<SearchActivity>(),
                sceneTransition(toolbar to getString(R.string.transition_appbar)))
        }

        setupNavigationDrawer()
        versionView.text = BuildConfig.VERSION_NAME

        with(geoMapsFragment) { setPaddingTop(dimen(R.dimen.maps_padding_top)) }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.contentContainer, geoMapsFragment, "geomap")
            .commit()

        addLocationButton.hide()
        observe(viewModel.houseNoLocation) {
            if (it == true) addLocationButton.show() else addLocationButton.hide()
        }
        btnBack.onClick {
            asm.visibility = View.VISIBLE;
            map.visibility = View.INVISIBLE;
            btnBack.show()
        }

    }

    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        checkHouseNoLocation()

        with(navView.getHeaderView(0)) {
            val user = auth(context).user!!
            find<TextView>(R.id.userDisplayNameView).text = user.displayName
            user.avatarUrl?.let { find<ImageView>(R.id.userAvartarView).load(Uri.parse(it)) }
            find<TextView>(R.id.orgDisplayNameView).text = org!!.displayName
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_main -> {
            }
            R.id.nav_follow -> browse("https://www.facebook.com/FFC.NECTEC/", true)
            R.id.nav_achivement, R.id.nav_manual -> {
                toast(R.string.under_construction)
            }
            //R.id.nav_report -> startActivity<ReportActivity>()
            R.id.nav_about -> startActivity<AboutActivity>()
            R.id.nav_device -> startActivity<DeviceMainActivity>()
            R.id.nav_settings -> startActivity<SettingsActivity>()
            R.id.nav_logout -> {
                auth(this).clear()
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun checkHouseNoLocation() {
        housesOf(org!!).houseNoLocation {
            onFound { viewModel.houseNoLocation.value = true }
            onNotFound { viewModel.houseNoLocation.value = false }
            onFail { }
        }
    }

    class MainViewModel : ViewModel() {
        val houseNoLocation = MutableLiveData<Boolean>()
    }
}
