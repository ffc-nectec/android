package ffc.app.asm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import ffc.app.R


class homeListAdapter(context: Context?, hl: ArrayList<homeModel>) : BaseAdapter() {

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
        var villageNo = homeItem.findViewById(R.id.villageNo) as TextView
        var homeNo = homeItem.findViewById(R.id.homeNo) as TextView
        var members = homeItem.findViewById(R.id.tvMembers) as TextView
        villageNo.setText(homeModel.villageNo.toString())
        homeNo.setText(homeModel.homeNo)
        members.setText(homeModel.members.toString())
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
