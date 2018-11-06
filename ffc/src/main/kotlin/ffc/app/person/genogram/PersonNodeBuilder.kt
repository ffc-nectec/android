package ffc.app.person.genogram

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import de.hdodenhof.circleimageview.CircleImageView
import ffc.android.load
import ffc.app.R
import ffc.genogram.GenderLabel
import ffc.genogram.Person
import ffc.genogram.android.GenogramNodeBuilder

internal class PersonNodeBuilder : GenogramNodeBuilder {

    override fun viewFor(person: Person, context: Context, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view = when (person.gender) {
            GenderLabel.MALE -> layoutInflater.inflate(R.layout.genogram_node_male_item, parent, false)
            GenderLabel.FEMALE -> layoutInflater.inflate(R.layout.genogram_node_female_item, parent, false)
        }
        val icon = view.findViewById<View>(R.id.icon)
        val identicon = Uri.parse("https://identicon.org?s=128&t=ffc-${person.idCard}")
        when (icon) {
            is ImageView -> icon.load(identicon)
            is CircleImageView -> icon.load(identicon)
        }
        icon.setOnClickListener {
            Toast.makeText(context, "Click ${person.firstname} ${person.lastname}", Toast.LENGTH_SHORT).show()
        }
        val name = view.findViewById<TextView>(R.id.name)
        name.text = person.firstname
        return view
    }
}
