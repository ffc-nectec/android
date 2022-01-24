/*
 * Copyright (c) 2018 NECT EC
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

package ffc.app.location

import android.app.Activity
//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
//import android.support.annotation.DrawableRes
//import android.support.design.widget.FloatingActionButton

import androidx.annotation.DrawableRes


import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPointStyle
import com.sembozdemir.permissionskt.handlePermissionsResult
import ffc.android.*
import ffc.app.R
import ffc.app.auth.auth
import ffc.app.dev
import ffc.app.familyFolderActivity
import ffc.app.util.alert.handle
import ffc.entity.gson.toJson
import ffc.entity.place.House
import me.piruin.geok.geometry.Feature
import me.piruin.geok.geometry.Point
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.intentFor
import org.json.JSONObject
import th.or.nectec.marlo.PointMarloFragment

class GeoMapsFragment : PointMarloFragment() {

    val REQ_ADD_LOCATION = 1032

    val preferZoomLevel = 17.0f

    private var addLocationButton: FloatingActionButton? = null
    private var btnFilter: FloatingActionButton? = null
    private val viewModel by lazy { viewModel<GeoViewModel>() }
    private val preference by lazy { GeoPreferences(requireContext(), org) }
    var window = PopupWindow()
    var isShow = false;
    var disease0 = true;
    var disease1 = true;
    var disease2 = true;
    var disease3 = true;
    var disease4 = true;
    var disease5 = true;
    var disease6 = true;
    var disease7 = true;
    private val org by lazy { familyFolderActivity.org }

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        setStartLocation(preference.lastCameraPosition)
        addLocationButton = requireActivity().find(R.id.addLocationButton)
        btnFilter = requireActivity().find(R.id.btnFilter)
        viewFinder.gone()
        hideToolsMenu()
        observeViewModel()
        if(window!=null){
            window.dismiss();
        }
        btnFilter!!.onClick {

            if(!isShow) {
                window = PopupWindow(context)
                val view = layoutInflater.inflate(R.layout.disease_filter, null)
                window.contentView = view

                window.showAtLocation(view,Gravity.NO_GRAVITY,40,420)

                var swDisease0 = view.findViewById<Switch>(R.id.swDisease0)
                var swDisease1 = view.findViewById<Switch>(R.id.swDisease1)
                var swDisease2 = view.findViewById<Switch>(R.id.swDisease2)
                var swDisease3 = view.findViewById<Switch>(R.id.swDisease3)
                var swDisease4 = view.findViewById<Switch>(R.id.swDisease4)
                var swDisease5 = view.findViewById<Switch>(R.id.swDisease5)
                var swDisease6 = view.findViewById<Switch>(R.id.swDisease6)
                var swDisease7 = view.findViewById<Switch>(R.id.swDisease7)
                swDisease0.isChecked = disease0
                swDisease1.isChecked = disease1
                swDisease2.isChecked = disease2
                swDisease3.isChecked = disease3
                swDisease4.isChecked = disease4
                swDisease5.isChecked = disease5
                swDisease6.isChecked = disease6
                swDisease7.isChecked = disease7
                swDisease0.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                    disease0 = b
                    disease1 = b
                    disease2 = b
                    disease3 = b
                    disease4 = b
                    disease5 = b
                    disease6 = b
                    disease7 = b
                    swDisease1.isChecked = b
                    swDisease2.isChecked = b
                    swDisease3.isChecked = b
                    swDisease4.isChecked = b
                    swDisease5.isChecked = b
                    swDisease6.isChecked = b
                    swDisease7.isChecked = b
                    observeViewModel()
                }
                swDisease1.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                     disease1 = b
                     clearSwith0Event(swDisease0);
                     setValueSwitch0(swDisease0,swDisease1,swDisease2,swDisease3,swDisease4,swDisease5,swDisease6,swDisease7)
                     observeViewModel()
                }
                 swDisease2.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
                     disease2 = b
                     clearSwith0Event(swDisease0);
                     setValueSwitch0(swDisease0,swDisease1,swDisease2,swDisease3,swDisease4,swDisease5,swDisease6,swDisease7)
                     observeViewModel()
                }
                swDisease3.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
                    disease3 = b
                    clearSwith0Event(swDisease0);
                    setValueSwitch0(swDisease0,swDisease1,swDisease2,swDisease3,swDisease4,swDisease5,swDisease6,swDisease7)
                    observeViewModel()
                }
                swDisease4.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
                    disease4 = b
                    clearSwith0Event(swDisease0);
                    setValueSwitch0(swDisease0,swDisease1,swDisease2,swDisease3,swDisease4,swDisease5,swDisease6,swDisease7)
                    observeViewModel()
                }
                swDisease5.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
                    disease5 = b
                    clearSwith0Event(swDisease0);
                    setValueSwitch0(swDisease0,swDisease1,swDisease2,swDisease3,swDisease4,swDisease5,swDisease6,swDisease7)
                    observeViewModel()
                }
                swDisease6.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
                    disease6 = b
                    clearSwith0Event(swDisease0);
                    setValueSwitch0(swDisease0,swDisease1,swDisease2,swDisease3,swDisease4,swDisease5,swDisease6,swDisease7)
                    observeViewModel()
                }
                swDisease7.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
                    disease7 = b
                    clearSwith0Event(swDisease0);
                    setValueSwitch0(swDisease0,swDisease1,swDisease2,swDisease3,swDisease4,swDisease5,swDisease6,swDisease7)
                    observeViewModel()
                }
                isShow = true;
            }
            else{
                window.dismiss();
                isShow = false;
            }
        }
    }
    private fun setValueSwitch0(sw0: Switch,sw1: Switch,sw2: Switch,sw3: Switch,sw4: Switch,sw5: Switch,sw6: Switch,sw7: Switch)
    {
        sw0.isChecked =  disease1 && disease2 && disease3 && disease4 && disease5 && disease6 && disease7
        disease0 = sw0.isChecked
        setSwitch0Event(sw0,sw1,sw2,sw3,sw4,sw5,sw6,sw7)
    }
    private fun clearSwith0Event(sw0: Switch){
        sw0.setOnCheckedChangeListener{compoundButton: CompoundButton, b: Boolean ->
        }
    }
    private fun setSwitch0Event(sw0:Switch,sw1:Switch,sw2:Switch,sw3:Switch,sw4:Switch,sw5:Switch,sw6:Switch,sw7:Switch){
            sw0.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            disease0 = b
            disease1 = b
            disease2 = b
            disease3 = b
            disease4 = b
            disease5 = b
            disease6 = b
            disease7 = b
            sw1.isChecked = b
            sw2.isChecked = b
            sw3.isChecked = b
            sw4.isChecked = b
            sw5.isChecked = b
            sw6.isChecked = b
            sw6.isChecked = b
            sw7.isChecked = b
            observeViewModel()
        }
    }
    private fun observeViewModel() {
        observe(viewModel.geojson) {
            var data = it?.copy()
            it?.let {
                if(data?.features?.size!! >0) {
                    val coordinates = (data?.features?.get(0)?.geometry as Point).coordinates
                    googleMap.animateCameraTo(
                        coordinates.latitude,
                        coordinates.longitude,
                        googleMap?.cameraPosition?.zoom?.takeIf { it >= preferZoomLevel }
                            ?: preferZoomLevel
                    )
                    googleMap.clear()
                    googleMap.setOnMapClickListener {
                        if(isShow) {
                            window.dismiss();
                            isShow=false;
                        }
                    }
                    /*

                    chronic = โรคเรื้อรัง
                    disable = มีคนพิการอยู่ในบ้าน
                     */
                    var lstDisease = ArrayList<String>()
                    if(disease1){
                        lstDisease.add("[]");
                    }
                    if(disease2){
                        lstDisease.add("chronic");
                    }
                    if(disease3){
                       lstDisease.add("chronic");
                    }
                    if(disease4){
                       lstDisease.add("chronic");
                    }
                    if(disease5){
                       lstDisease.add("chronic");
                    }
                    if(disease6){
                       lstDisease.add("disable");
                    }
                    if(disease7){
                        lstDisease.add("elder");
                    }
                    if(lstDisease.size==0){
                        var features = data?.features?.filter { it.properties?.tags!!.indexOf("----")>-1 }
                        Log.d("Filter----> :",features.size.toString());
                        data.features = features!!
                    }

                    if(lstDisease.size==1){
                        //var features = data?.features?.filter { it.properties?.tags!!.indexOf(lstDisease[0].toString())>-1 }
                        var features = data?.features?.filter {
                            it.properties?.tags.toString().indexOf(lstDisease[0])>-1 }
                        Log.d("Filter----> :",features.size.toString());
                        data.features = features!!
                    }
                    if(lstDisease.size==2){
                        var features = data?.features?.filter {
                                   it.properties?.tags!!.toString().indexOf(lstDisease[0].toString())>-1
                                || it.properties?.tags!!.toString().indexOf(lstDisease[1].toString())>-1
                        }
                        Log.d("Filter----> :",features.size.toString());
                        data.features = features!!
                    }
                    if(lstDisease.size==3){
                        var features = data?.features?.filter {
                                   it.properties?.tags!!.toString().indexOf(lstDisease[0].toString())>-1
                                || it.properties?.tags!!.toString().indexOf(lstDisease[1].toString())>-1
                                || it.properties?.tags!!.toString().indexOf(lstDisease[2].toString())>-1}
                        Log.d("Filter----> :",features.size.toString());
                        data.features = features!!
                    }
                    if(lstDisease.size==4){
                        var features = data?.features?.filter {
                               it.properties?.tags!!.toString().indexOf(lstDisease[0].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[1].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[2].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[3].toString())>-1
                        }
                        Log.d("Filter----> :",features.size.toString());
                        data.features = features!!
                    }
                    if(lstDisease.size==5){
                        var features = data?.features?.filter {
                               it.properties?.tags!!.toString().indexOf(lstDisease[0].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[1].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[2].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[3].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[4].toString())>-1
                        }
                        Log.d("Filter----> :",features.size.toString());
                        data.features = features!!
                    }
                    if(lstDisease.size==6){
                        var features = data?.features?.filter {
                               it.properties?.tags!!.toString().indexOf(lstDisease[0].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[1].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[2].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[3].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[4].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[5].toString())>-1
                        }
                        Log.d("Filter----> :",features.size.toString());
                        data.features = features!!
                    }
                    if(lstDisease.size==7){
                        var features = data?.features?.filter {
                               it.properties?.tags!!.toString().indexOf(lstDisease[0].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[1].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[2].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[3].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[4].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[5].toString())>-1
                            || it.properties?.tags!!.toString().indexOf(lstDisease[6].toString())>-1
                        }
                        Log.d("Filter----> :",features.size.toString());
                        data.features = features!!
                    }
                    addGeoJsonLayer(GeoJsonLayer(googleMap, JSONObject(data?.toJson())));
                    preference.geojsonCache = data
                }
            }
        }
        observe(viewModel.exception) {
            it?.let { familyFolderActivity.handle(it) }
        }
    }

    private fun setStartLocation(lastPosition: CameraPosition?) {
        if (lastPosition != null) {
            setStartLocation(lastPosition.target, lastPosition.zoom)
        } else {
            setStartLocation(LatLng(13.76498, 100.538335), 5.0f)
            setStartAtCurrentLocation(true)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        super.onMapReady(googleMap)
        addLocationButton?.setOnClickListener {
            val intent = intentFor<MarkLocationActivity>(
                "target" to googleMap!!.cameraPosition.target,
                "zoom" to googleMap.cameraPosition.zoom
            )
            startActivityForResult(intent, REQ_ADD_LOCATION)
        }
        askMyLocationPermission()
        loadGeoJson()
    }

    private fun loadGeoJson() {
        placeGeoJson(familyFolderActivity.org!!).all {
            onFound {
                viewModel.geojson.value = it;
            }
            onFail {
                dev { viewModel.geojson.value = context?.rawAs(R.raw.place) }
                viewModel.exception.value = it
            }
        }
    }

    private fun addGeoJsonLayer(layer: GeoJsonLayer) {
        var i= 0;
        with(layer) {
            features.forEach {
//                it.pointStyle = GeoJsonPointStyle().apply {
//                    icon = if (it.getProperty("haveChronic") == "true") chronicHomeIcon else homeIcon
//                    title = "บ้านเลขที่ ${it.getProperty("no")}"
//                    snippet = it.getProperty("coordinates")?.trimMargin()
//                }

                i = i +1;
                Log.d("tags---->:"," no:"+i+" id:"+it.getProperty("id")+ " --> length:"+it.getProperty("tags").toString().length.toString()+" -> "+it.getProperty("tags").toString());
                if(disease1  && it.getProperty("tags").indexOf("[]")>-1) {
                    it.pointStyle = GeoJsonPointStyle().apply {
                        icon =  homeIcon
                        title = "บ้านเลขที่ ${it.getProperty("no")}"
                        snippet = it.getProperty("coordinates")?.trimMargin()
                    }
                }
                else if(disease2 &&  it.getProperty("tags").indexOf("chronic")>-1){
                    it.pointStyle = GeoJsonPointStyle().apply {
                        icon =  chronicHomeIcon
                        title = "บ้านเลขที่ ${it.getProperty("no")}"
                        snippet = it.getProperty("coordinates")?.trimMargin()
                    }
                }
                else if(disease3 &&  it.getProperty("tags").indexOf("chronic")>-1){
                    it.pointStyle = GeoJsonPointStyle().apply {
                        icon =  chronicHomeIcon
                        title = "บ้านเลขที่ ${it.getProperty("no")}"
                        snippet = it.getProperty("coordinates")?.trimMargin()
                    }
                }
                else if(disease4 &&  it.getProperty("tags").indexOf("chronic")>-1){
                    it.pointStyle = GeoJsonPointStyle().apply {
                        icon =  chronicHomeIcon
                        title = "บ้านเลขที่ ${it.getProperty("no")}"
                        snippet = it.getProperty("coordinates")?.trimMargin()
                    }
                }
                else if(disease5 &&  it.getProperty("tags").indexOf("chronic")>-1){
                    it.pointStyle = GeoJsonPointStyle().apply {
                        icon =  chronicHomeIcon
                        title = "บ้านเลขที่ ${it.getProperty("no")}"
                        snippet = it.getProperty("coordinates")?.trimMargin()
                    }
                }
                else if(disease6 &&  it.getProperty("tags").indexOf("disable")>-1){
                    it.pointStyle = GeoJsonPointStyle().apply {
                        icon =  disableHomeIcon
                        title = "บ้านเลขที่ ${it.getProperty("no")}"
                        snippet = it.getProperty("coordinates")?.trimMargin()
                    }
                }
                else if(disease7 &&  it.getProperty("tags").indexOf("elder")>-1){
                    it.pointStyle = GeoJsonPointStyle().apply {
                        icon =  oldHomeIcon
                        title = "บ้านเลขที่ ${it.getProperty("no")}"
                        snippet = it.getProperty("coordinates")?.trimMargin()
                    }
                }
                else{
                    it.pointStyle = GeoJsonPointStyle().apply {
                        icon =  null
                        title = null
                        snippet = "0,0"
                    };
                }
            }
            setOnFeatureClickListener { feature ->
                val user = auth(context!!).user!!
                if(feature!=null) {
                    val intent = intentFor<HouseActivity>("houseId" to feature.getProperty("id"))
                    startActivityForResult(intent, REQ_ADD_LOCATION, activity!!.sceneTransition())
                }
            }
            addLayerToMap()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_ADD_LOCATION -> {
                if (resultCode == Activity.RESULT_OK) loadGeoJson()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        activity!!.handlePermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPause() {
        super.onPause()
        googleMap?.cameraPosition?.let { preference.lastCameraPosition = it }
    }

    fun bitmapOf(@DrawableRes resId: Int) = BitmapDescriptorFactory.fromBitmap(context!!.drawable(resId).toBitmap())

    private val homeIcon by lazy { bitmapOf(R.drawable.ic_marker_home_green_24dp) }

    private val chronicHomeIcon by lazy { bitmapOf(R.drawable.ic_marker_home_red_24dp) }

    private val disableHomeIcon by lazy { bitmapOf(R.drawable.ic_marker_home_purple_24dp) }

    private val oldHomeIcon by lazy { bitmapOf(R.drawable.ic_marker_home_gray_24dp) }

    class GeoViewModel : ViewModel() {
        val geojson = MutableLiveData<FeatureCollectionFilter<House>>()
        val lstHome = MutableLiveData<List<House>>()
        val exception = MutableLiveData<Throwable>()
    }
}
