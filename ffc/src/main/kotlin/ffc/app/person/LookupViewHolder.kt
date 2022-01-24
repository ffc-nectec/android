package ffc.app.person

//import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ffc.android.layoutInflater
import ffc.app.R
import kotlinx.android.synthetic.main.lookup_item.view.lookupCaption
import kotlinx.android.synthetic.main.lookup_item.view.lookupId
import kotlinx.android.synthetic.main.lookup_item.view.lookupLabel
import kotlinx.android.synthetic.main.lookup_item.view.lookupName

class LookupViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(parent.layoutInflater.inflate(R.layout.lookup_item, parent, false)) {

    fun bind(id: String, name: String, caption: String? = null, label: String? = null) {
        with(itemView) {
            lookupId.text = id
            lookupName.text = name
            lookupCaption.visibility = if (caption != null) View.VISIBLE else View.GONE
            lookupCaption.text = caption
            lookupLabel.visibility = if (label != null) View.VISIBLE else View.GONE
            lookupLabel.text = label
        }
    }
}
