package ffc.app.asm

import android.os.Bundle
//import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ffc.android.observe
import ffc.android.sceneTransition
import ffc.android.viewModel
import ffc.app.R
import ffc.app.auth.auth
import ffc.app.location.FeatureCollectionFilter
import ffc.app.location.GeoMapsFragment
import ffc.app.location.HouseActivity
import ffc.app.location.placeGeoJson
import ffc.entity.place.House
import org.jetbrains.anko.support.v4.intentFor
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class AsmNotCareFragment : Fragment() {

    private val viewModel by lazy { viewModel<GeoMapsFragment.GeoViewModel>() }
    var lvHomeList : ListView? =null
    var txtAsmSearch: EditText?=null
    var tvAmount : TextView?=null
    var myData = ArrayList<homeModel>()
    val REQ_ADD_LOCATION = 1032
    var data:List<House>? = null;
    var hl : ArrayList<homeModel> = ArrayList<homeModel>()
    private fun observeViewModel() {
        observe(viewModel.lstHome) {
            data = it;
            it?.let {
                if(data!!.size!! >0) {
                    for(i in 0 .. data!!.size-1){

                            var hM: homeModel = homeModel()
                            hM.homeNo = data?.get(i)?.no.toString()
                            hM.vilageName = data?.get(i)?.villageName.toString()
                            hM.id= data?.get(i)?.id.toString()
                            hl.add(hM)
                    };
                    var hlSort = hl.sortedWith(compareBy({ it.homeNo }))
                    var myData = ArrayList<homeModel>()
                    myData.addAll(hlSort)
                    tvAmount!!.text = "จำนวนบ้าน: "+ myData.size.toString();
                    var hlAdapter :homeListAdapter = homeListAdapter(context, myData)
                    lvHomeList!!.adapter = hlAdapter;
                }
            }
        }
        observe(viewModel.exception) {
            it?.let {
                Toast.makeText(context,it.message, Toast.LENGTH_LONG).show();
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_asm_not_care, container, false)
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
        lvHomeList!!.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, pos, l ->
            var lblhouseId = view.findViewById<TextView>(R.id.lblhomeId);
            var houseId = lblhouseId.text
            val intent = intentFor<HouseActivity>("houseId" to houseId)
            startActivityForResult(intent, REQ_ADD_LOCATION, requireActivity().sceneTransition())
        })
        loadGeoJson();
        observeViewModel();
        return view
    }

    private fun loadGeoJson() {
        placeGeoJson(auth(requireContext()).org!!).noLocation {
            onFound {
                viewModel.lstHome.value = it;
            }
            onFail {
                // dev { viewModel.geojson.value = context?.rawAs(R.raw.place) }
                viewModel.exception.value = it
            }
        }
    }


}
