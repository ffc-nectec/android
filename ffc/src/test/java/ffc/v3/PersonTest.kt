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

import ffc.entity.Address
import ffc.entity.Chronic
import ffc.entity.Person
import ffc.entity.ThaiCitizenId
import ffc.entity.ThaiHouseholdId
import ffc.v3.util.defaultGson
import ffc.v3.util.parseTo
import me.piruin.geok.LatLng
import org.joda.time.LocalDate
import org.junit.Test

class PersonTest {

  val gson = defaultGson

  @Test
  fun toJson() {
    val person = Person().apply {
      hospCode = "15032"
      pid = 1
      identities.add(ThaiCitizenId("1015448452554"))
      prename = "นาย"
      firstname = "พิรุณ"
      lastname = "พานิชผล"

      house = Address().apply {
        identity = ThaiHouseholdId("54520015001")
        no = "510/45"
        road = "รังสิต-นครนายก"
        tambon = "คลองหนึ่ง"
        ampur = "คลองหลวง"
        changwat = "ปุทมธานี"
        coordinates = LatLng(10.0, 120.0)
      }
      chronics?.add(Chronic("N18.3", LocalDate(2015, 5, 21)))
      chronics?.add(Chronic("I10", LocalDate(2016, 5, 21)))
    }

    print(gson.toJson(person))
  }

  @Test
  fun fromJson() {
    val json = """
{
  "hospCode": "15032",
  "pid": 1,
  "prename": "นาย",
  "firstname": "พิรุณ",
  "lastname": "พานิชผล",
  "birthData": "1950-09-21",
  "identities": [
    {
      "type": "thailand-citizen-id",
      "id": "1015448452554"
    }
  ],
  "house": {
    "identity": {
      "type": "thailand-household-id",
      "id": "54520015001"
    },
    "type": "House",
    "no": "510/45",
    "road": "รังสิต-นครนายก",
    "tambon": "คลองหนึ่ง",
    "ampur": "คลองหลวง",
    "changwat": "ปุทมธานี",
    "coordinate": {
      "latitude": 10.0,
      "longitude": 120.0
    },
    "id": -1493233147101929696
  },
  "chronics": [
    {
      "status": "active",
      "idc10": "N18.3",
      "diagDate": "2015-05-21"
    },
    {
      "status": "active",
      "idc10": "I10",
      "diagDate": "2016-05-21"
    }
  ],
  "id": 3473489528307407616
}
      """.trimIndent()

    val person = json.parseTo<Person>(gson)!!

    assert(person.identities[0] == ThaiCitizenId("1015448452554"))
  }
}
