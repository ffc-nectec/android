package ffc.app.asm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import ffc.app.R
import kotlinx.android.synthetic.main.activity_visit.view.*
import kotlinx.android.synthetic.main.fragment_asm_care.*
import kotlinx.android.synthetic.main.fragment_asm_care.view.*

/**
 * A simple [Fragment] subclass.
 */
class AsmCareFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_asm_care, container, false)
        val lvHomeList = view.findViewById(R.id.lvHomeList) as ListView
        var hl : ArrayList<homeModel> = ArrayList<homeModel>()
        var hM : homeModel = homeModel()

        hM.homeNo = 10.toString()
        hM.members = 2
        hM.villageNo = 2
        hl.add(hM)

        hM = homeModel()
        hM.homeNo = 2.toString()
        hM.members = 5
        hM.villageNo = 1
        hl.add(hM)

        hM = homeModel()
        hM.homeNo = 12.toString()
        hM.members = 3
        hM.villageNo = 5
        hl.add(hM)

        var hlAdapter :homeListAdapter = homeListAdapter(context,hl)
        lvHomeList.adapter = hlAdapter;
        return view
    }



}
