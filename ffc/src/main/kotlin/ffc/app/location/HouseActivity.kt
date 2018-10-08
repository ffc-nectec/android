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

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import ffc.android.toast
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.person.PersonAdapter
import ffc.app.person.startPersonActivityOf
import ffc.entity.House
import kotlinx.android.synthetic.main.activity_house.recycleView
import org.jetbrains.anko.toast

class HouseActivity : FamilyFolderActivity() {

    val houseId: String
        get() = intent.getStringExtra("houseId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(recycleView) {
            layoutManager = LinearLayoutManager(context)
            adapter = PersonAdapter(listOf()) {
                onItemClick { startPersonActivityOf(it) }
            }
        }

        House(houseId).resident(org!!.id) {
            onFound {
                (recycleView.adapter as PersonAdapter).update(it)
            }
            onNotFound {
                toast("ไม่พบผู้อยู่อาศัย")
            }
            onFail {
                toast(it)
            }
        }
    }
}
