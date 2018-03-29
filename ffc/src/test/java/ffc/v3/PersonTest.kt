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

package ffc.v3

import com.fatboyindustrial.gsonjodatime.Converters
import com.google.gson.GsonBuilder
import org.joda.time.LocalDate
import org.junit.Test

class PersonTest {

  @Test
  fun toJson() {
    val person = Person().apply {
      hospCode = "15032"
      pid = 1
      identities.add(ThaiCitizenId("1015448452554"))
      prename = "นาย"
      firstname = "พิรุณ"
      lastname = "พานิชผล"
      birthData = LocalDate(1950, 9, 21)

      house = Address().apply {
        identity = ThaiHouseholdId("54520015001")
        no = "510/45"
        road = "รังสิต-นครนายก"
        tambon = "คลองหนึ่ง"
        ampur = "คลองหลวง"
        changwat = "ปุทมธานี"
        latlng = arrayListOf(120.0, 10.0)
      }
      chronics.add(Chronic("N18.3", LocalDate(2015, 5, 21)))
      chronics.add(Chronic("I10", LocalDate(2016, 5, 21)))
    }

    val gson = Converters.registerAll(GsonBuilder()).create()

    print(gson.toJson(person))
  }
}
