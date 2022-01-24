/*
 * Copyright (c) 2018 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ffc.app.person
import android.app.Activity
import android.net.Uri
//import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import ffc.android.getString
import ffc.android.layoutInflater
import ffc.android.load
import ffc.android.tag
import ffc.app.R
import ffc.app.auth.auth
import ffc.app.health.analyze.toIconTitlePair
import ffc.app.util.AdapterClickListener
import ffc.entity.Person
import ffc.entity.User
import kotlinx.android.synthetic.main.person_list_item.view.*
import org.jetbrains.anko.dip
import timber.log.Timber

class PersonAdapter(
    var persons: List<Person>,
    var limit: Int = Int.MAX_VALUE,
    onClickDsl: AdapterClickListener<Person>.() -> Unit
) : RecyclerView.Adapter<PersonAdapter.PersonHolder>() {

    val listener = AdapterClickListener<Person>().apply(onClickDsl)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        val view = parent.layoutInflater.inflate(R.layout.person_list_item, parent, false)
        return PersonHolder(view)
    }

    override fun getItemCount(): Int {
        Timber.d("person size ${persons.size}")
        return persons.size.takeIf { it < limit } ?: limit
    }

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        holder.bind(persons[position], listener)
    }

    fun update(update: List<Person>) {
        this.persons = update
        notifyDataSetChanged()
    }

    class PersonHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(person: Person, listener: AdapterClickListener<Person>) {
            with(person) {
                itemView.personNameView.text = name
                itemView.personAgeView.text = "$age ปี"
                itemView.personDeadLabel.visibility = if (isDead) View.VISIBLE else View.GONE
                itemView.personImageView.setImageResource(R.drawable.ic_account_circle_black_24dp)
                itemView.personStatus.removeAllViews()
                //Log.d("disabilities:",person.disabilities.toString());
                //Log.d("Tags:",person.tags.toString());
                if(person.disabilities.size>0) {
                    val layoutParam = LinearLayout.LayoutParams(itemView.dip(16), itemView.dip(16)).apply {
                        marginStart = itemView.dip(4)
                        marginEnd = itemView.dip(4)
                    }
                    itemView.personStatus.addView(ImageView(itemView.context).apply {
                        setImageResource(R.drawable.ic_wheelchair)
                    }, layoutParam)
                }
                person.healthAnalyze?.result?.filterValues { it.haveIssue }?.forEach {
                    val layoutParam = LinearLayout.LayoutParams(itemView.dip(16), itemView.dip(16)).apply {
                        marginStart = itemView.dip(4)
                        marginEnd = itemView.dip(4)
                    }

                    val pair = it.value.issue.toIconTitlePair() ?: return@forEach
                    itemView.personStatus.addView(ImageView(itemView.context).apply {
                        setImageResource(pair.first)
                        contentDescription = context.getString(pair.second)
                    }, layoutParam)
                }
                avatarUrl?.let { itemView.personImageView.load(Uri.parse(it)) }
               listener.bindOnItemClick(itemView, person)
            }
        }
    }
}
