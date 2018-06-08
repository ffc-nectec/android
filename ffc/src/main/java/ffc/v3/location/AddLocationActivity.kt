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

package ffc.v3.location

import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import ffc.v3.Address
import ffc.v3.BaseActivity
import ffc.v3.R
import ffc.v3.R.id
import ffc.v3.R.layout
import ffc.v3.util.find
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.support.v4.dimen
import org.jetbrains.anko.support.v4.toast
import th.or.nectec.marlo.PointMarloFragment

class AddLocationActivity : BaseActivity() {

  val REQ_TARGET = 10293

  lateinit var targetPlace: Address

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(layout.activity_add_location)

    val mapFragment = supportFragmentManager.find<PointMarloFragment>(id.mapFragment)
    with(mapFragment) {
      setPaddingTop(dimen(R.dimen.maps_padding_top))
      setMaxPoint(1)
      setStartLocation(
        intent.getParcelableExtra("target") as LatLng,
        intent.getFloatExtra("zoom", 15.0f)
      )
      setOnPointChange {
        toast("Marked")
      }
    }
    startActivityForResult<HouseNoLocationActivtiy>(REQ_TARGET)
  }

}
