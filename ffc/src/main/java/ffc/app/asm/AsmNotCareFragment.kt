package ffc.app.asm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import ffc.app.R

/**
 * A simple [Fragment] subclass.
 */
class AsmNotCareFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_asm_not_care, container, false)
        val lvHomeList = view.findViewById(R.id.lvHomeList) as ListView
        var hl : ArrayList<homeModel> = ArrayList<homeModel>()
        var hM : homeModel = homeModel()

        hM.homeNo = 10.toString()
        hM.members = 4
        hM.villageNo = 6
        hl.add(hM)

        hM = homeModel()
        hM.homeNo = 2.toString()
        hM.members = 2
        hM.villageNo = 3
        hl.add(hM)

        hM = homeModel()
        hM.homeNo = 2.toString()
        hM.members = 2
        hM.villageNo = 1
        hl.add(hM)

        hM = homeModel()
        hM.homeNo = 2.toString()
        hM.members = 7
        hM.villageNo = 12
        hl.add(hM)

        var hlAdapter :homeListAdapter = homeListAdapter(context,hl)
        lvHomeList.adapter = hlAdapter;
        return view

    }

}
