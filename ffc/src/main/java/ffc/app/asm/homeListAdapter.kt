package ffc.app.asm

import android.content.Context
import android.content.Intent
//import android.support.v4.app.ActivityCompat.startActivityForResult
//import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ffc.android.onClick
import ffc.android.sceneTransition
import ffc.app.R
import ffc.app.location.HouseActivity
import org.jetbrains.anko.startActivity


class homeListAdapter(context: Context?, hl: ArrayList<homeModel>) : BaseAdapter() {

    var REQ_ADD_LOCATION = 1032;
    private var mHomeList =  hl
    private var mInflater: LayoutInflater? = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var  homeModel = mHomeList.get(position)
        var homeItem:LinearLayout;
        if(convertView != null)
        {
            homeItem = convertView as LinearLayout
        }
        else
        {
            homeItem  = mInflater!!.inflate(R.layout.homeitem,null)  as LinearLayout
        }
        var homeId = homeItem.findViewById(R.id.lblhomeId) as TextView
//        var villageNo = homeItem.findViewById(R.id.villageNo) as TextView
        var homeNo = homeItem.findViewById(R.id.homeNo) as TextView
        var villageName = homeItem.findViewById(R.id.tvVillageName) as TextView
//        villageNo.setText(homeModel.villageNo.toString())
        homeNo.setText(homeModel.homeNo)
        villageName.setText(homeModel.vilageName.toString())
        homeId.setText(homeModel.id);

        return homeItem
    }

    override fun getItem(position: Int): Any {
        return mHomeList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0;
    }

    override fun getCount(): Int {
        return mHomeList.size;
    }

}
