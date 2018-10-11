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

package ffc.app.location

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.transition.Slide
import android.view.Gravity
import android.widget.ImageView
import ffc.android.allowTransitionOverlap
import ffc.android.enter
import ffc.android.excludeSystemView
import ffc.android.exit
import ffc.android.setTransition
import ffc.android.toast
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.person.PersonAdapter
import ffc.app.person.startPersonActivityOf
import ffc.entity.House
import kotlinx.android.synthetic.main.activity_house.appbar
import kotlinx.android.synthetic.main.activity_house.emptyView
import kotlinx.android.synthetic.main.activity_house.recycleView
import org.jetbrains.anko.find

class HouseActivity : FamilyFolderActivity() {

    val houseId: String
        get() = intent.getStringExtra("houseId")

    lateinit var house: House

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house)

        setTransition {
            enterTransition = Slide(Gravity.BOTTOM).enter().excludeSystemView()
            exitTransition = Slide(Gravity.START).exit()
            reenterTransition = Slide(Gravity.START).enter()
            allowTransitionOverlap = false
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "บ้านเลขที่ ?"

        emptyView.loading().show()

        with(recycleView) {
            layoutManager = LinearLayoutManager(context)
            adapter = PersonAdapter(listOf()) {
                onItemClick {
                    startPersonActivityOf(it,
                        appbar to getString(R.string.transition_appbar),
                        itemView to getString(R.string.transition_card),
                        itemView.find<ImageView>(R.id.personImageView) to getString(R.string.transition_person_profile)
                    )
                }
            }
        }

        housesOf(org!!).house(houseId) {
            onFound { bind(it) }
            onNotFound {
                toast(Resources.NotFoundException("Not found House"))
                finish()
            }
            onFail { toast(it) }
        }
    }

    fun bind(house: House) {
        this.house = house
        supportActionBar?.title = "บ้านเลขที่ ${house.no}"
        house.resident(org!!.id) {
            onFound {
                (recycleView.adapter as PersonAdapter).update(it)
                emptyView.content().show()
            }
            onNotFound {
                emptyView.empty().show()
            }
            onFail {
                emptyView.error(it).show()
            }
        }
    }
}
