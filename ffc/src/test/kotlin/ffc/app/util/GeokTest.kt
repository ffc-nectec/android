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

package ffc.app.util

import ffc.entity.House
import ffc.entity.ThaiHouseholdId
import ffc.entity.gson.parseTo
import me.piruin.geok.geometry.FeatureCollection
import me.piruin.geok.geometry.Point
import org.amshove.kluent.shouldEqual
import org.junit.Test

class GeokTest {
    @Test
    fun featureFromJson() {
        val json = """
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "properties": {
        "id": 10000124,
        "identity": {
          "type": "thailand-household-id",
          "id": "54520015001"
        },
        "haveChronics": true,
        "type": "House",
        "no": "510/45",
        "road": "รังสิต-นครนายก",
        "tambon": "คลองหนึ่ง",
        "ampur": "คลองหลวง",
        "changwat": "ปุทมธานี",
        "location": {
          "type": "Point",
          "coordinates": [
            102.0,
            13.0
          ]
        }
      },
      "geometry": {
        "type": "Point",
        "coordinates": [
          102.0,
          13.0
        ]
      }
    },
    {
      "type": "Feature",
      "properties": {
        "id": 10000124,
        "identity": {
          "type": "thailand-household-id",
          "id": "54520015001"
        },
        "haveChronics": true,
        "type": "House",
        "no": "112 ",
        "road": "รังสิต-นครนายก",
        "tambon": "คลองหนึ่ง",
        "ampur": "คลองหลวง",
        "changwat": "ปุทมธานี",
        "location": {
          "type": "Point",
          "coordinates": [
            102.0,
            13.1
          ]
        }
      },
      "geometry": {
        "type": "Point",
        "coordinates": [
          102.0,
          13.1
        ]
      }
    }
  ]
}
""".trimIndent()

        val collection = json.parseTo<FeatureCollection<House>>()
        val address = collection.features[0].properties

        with(address!!) {
            id shouldEqual "10000124"
            identity shouldEqual ThaiHouseholdId("54520015001")
            no shouldEqual "510/45"
            location shouldEqual Point(13.0, 102.0)
        }
    }
}
