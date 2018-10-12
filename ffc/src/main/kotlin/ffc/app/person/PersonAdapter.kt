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

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import ffc.android.layoutInflater
import ffc.android.load
import ffc.app.R
import ffc.app.util.AdapterClickListener
import ffc.entity.Person
import kotlinx.android.synthetic.main.person_list_item.view.personAgeView
import kotlinx.android.synthetic.main.person_list_item.view.personImageView
import kotlinx.android.synthetic.main.person_list_item.view.personNameView

class PersonAdapter(
    var persons: List<Person>,
    onClickDsl: AdapterClickListener<Person>.() -> Unit
) : RecyclerView.Adapter<PersonAdapter.PersonHolder>() {

    val listener = AdapterClickListener<Person>().apply(onClickDsl)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        val view = parent.layoutInflater.inflate(R.layout.person_list_item, parent, false)
        return PersonHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("adapter", "person size ${persons.size}")
        return persons.size
    }

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        holder.bind(persons[position], listener)
        Log.d("adapter", "bind person $position")
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
                avatarUrl?.let { itemView.personImageView.load(Uri.parse(it)) }
                listener.bindOnItemClick(itemView, person)
            }
        }
    }
}
