package ffc.app.person

import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.widget.GridLayoutManager
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ffc.android.onClick
import ffc.app.R
import ffc.app.person.genogram.GenogramActivity
import ffc.entity.Person
import kotlinx.android.synthetic.main.person_relationship_fragment.genogramButton
import kotlinx.android.synthetic.main.person_relationship_fragment.recycleView
import org.jetbrains.anko.startActivity

class RelationshipFragment : Fragment() {

    var person: Person? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.person_relationship_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        person?.let {
            with(recycleView) {
                adapter = RelationshipAdapter(it.relationships.sortedBy { it.relate })
                layoutManager = GridLayoutManager(requireContext(), 2)
            }

            genogramButton.onClick {
                requireActivity().startActivity<GenogramActivity>("personId" to person?.id)
            }
        }
    }

    class RelationshipAdapter(
        val relationships: List<Person.Relationship>
    ) : RecyclerView.Adapter<RelationViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, type: Int): RelationViewHolder {
            return RelationViewHolder(RelationshipView(parent.context))
        }

        override fun getItemCount() = relationships.size

        override fun onBindViewHolder(holder: RelationViewHolder, position: Int) {
            with((holder.itemView as RelationshipView)) {
                relationship = relationships[position]
                onClick {
                    context!!.startActivity<PersonActivitiy>("personId" to it.relationship!!.id)
                }
            }
        }
    }

    class RelationViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
