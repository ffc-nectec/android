package ffc.app.asm

import android.content.Intent
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ffc.android.observe
import ffc.android.sceneTransition
import ffc.android.viewModel
import ffc.app.R
import ffc.app.auth.auth
import ffc.app.location.GeoMapsFragment
import ffc.app.location.HouseActivity
import ffc.app.location.placeGeoJson
import ffc.genogram.Family
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.intentFor
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class AsmCareFragment : Fragment() {

    private val viewModel by lazy { viewModel<GeoMapsFragment.GeoViewModel>() }
    var lvHomeList : ListView? =null
    var txtAsmSearch: EditText?=null
    var tvAmount : TextView?=null
    var myData = ArrayList<homeModel>()
    val REQ_ADD_LOCATION = 1032
    var hl : ArrayList<homeModel> = ArrayList<homeModel>()
    private fun observeViewModel() {
        observe(viewModel.geojson) {
            var data = it?.copy()
            it?.let {
                if(data?.features?.size!! >0) {
                    val auth = context?.let { it1 -> auth(it1) }
                     var asmId = auth!!.user?.id;
                     for(i in 0 .. data?.features.size-1){
                         if(data?.features.get(i).properties!!.allowUserId.size>0) {
                             Log.d("allowUserId", data?.features.get(i).properties!!.allowUserId.toString() + " -->" + asmId);
                             Log.d("indexof", data?.features.get(i).properties!!.allowUserId.indexOf(asmId).toString());
                             if (data?.features.get(i).properties!!.allowUserId.indexOf(asmId) > -1) {
                                 var hM: homeModel = homeModel()
                                 hM.homeNo = data?.features.get(i).properties!!.no.toString()
                                 hM.vilageName = data?.features.get(i).properties!!.villageName.toString()
                                 hM.id = data?.features.get(i).properties!!.id
                                 hl.add(hM)
                             }
                         }
                     };
                    // var hlSort = hl.sortedWith(compareBy({ it.homeNo }))
                    var hlSort = hl.sortedWith(compareBy({ it.homeNo }))

                    myData.addAll(hlSort)
                    tvAmount!!.text = "จำนวนบ้าน: "+ myData.size.toString();
                    var hlAdapter :homeListAdapter = homeListAdapter(context, myData)
                    lvHomeList!!.adapter = hlAdapter
                }
            }
        }
        observe(viewModel.exception) {
            it?.let {
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_asm_care, container, false)
        tvAmount = view.findViewById(R.id.tvAmount) as TextView
        lvHomeList = view.findViewById(R.id.lvHomeList) as ListView
        txtAsmSearch = view.findViewById(R.id.txtAsmSearch) as EditText
        val field: TextWatcher = object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                var hlFilter = hl.filter { it.homeNo.contains(txtAsmSearch!!.text) || it.vilageName.contains(txtAsmSearch!!.text)}
                var hlAdapter :homeListAdapter = homeListAdapter(context, hlFilter as ArrayList<homeModel>)
                lvHomeList!!.adapter = hlAdapter;
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }
        txtAsmSearch!!.addTextChangedListener(field);
        lvHomeList!!.setOnItemClickListener(OnItemClickListener { adapterView, view, pos, l ->
             var houseId=view.findViewById<TextView>(R.id.lblhomeId);
             val intent = intentFor<HouseActivity>("houseId" to houseId.text.toString())
             startActivityForResult(intent, REQ_ADD_LOCATION, requireActivity().sceneTransition())
        })
        loadGeoJson();
        observeViewModel();
        return view
    }
    private fun loadGeoJson() {
        placeGeoJson(auth(requireContext()).org!!).all {
            onFound {
                viewModel.geojson.value = it;
            }
            onFail {
               // dev { viewModel.geojson.value = context?.rawAs(R.raw.place) }
                viewModel.exception.value = it
            }
        }
    }


}
