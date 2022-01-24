package ffc.app.person

import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.widget.DividerItemDecoration
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ffc.app.R
import ffc.app.util.datetime.toBuddistString
import ffc.entity.Lang
import ffc.entity.Person
import ffc.entity.healthcare.Disease
import ffc.entity.healthcare.Icd10
import kotlinx.android.synthetic.main.person_death_info_fragment.personDeathCause
import kotlinx.android.synthetic.main.person_death_info_fragment.personDeathDate

class DeathFragment : Fragment() {

    var death: Person.Death? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.person_death_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        death?.let {
            personDeathDate.text = it.date.toBuddistString()
            with(personDeathCause) {
                adapter = DeathCauseAdapter(it.causes)
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }
    }

    class DeathCauseAdapter(private val cause: List<Disease>) : RecyclerView.Adapter<LookupViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, type: Int) = LookupViewHolder(parent)

        override fun getItemCount(): Int = cause.size

        override fun onBindViewHolder(holder: LookupViewHolder, position: Int) {
            val icd10 = cause[position] as Icd10
            val anotherLang = icd10.translation[Lang.en] ?: icd10.translation[Lang.th]
            holder.bind(icd10.icd10, icd10.name, anotherLang, if (position == 0) "สาเหตุหลัก" else null)
        }
    }
}
