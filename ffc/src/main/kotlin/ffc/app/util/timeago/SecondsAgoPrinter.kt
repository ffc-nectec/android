/*
 * Copyright (c) 2015 NECTEC
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

package ffc.app.util.timeago

internal class SecondsAgoPrinter(private val currentTimer: CurrentTimer) : TimePrettyPrinter {

    override fun print(referenceTime: Long): String {
        val currentTimeInMills = currentTimer.inMills
        val diff = currentTimeInMills - referenceTime
        val sec = diff / SECOND_IN_MILLS
        return if (sec > 30) {
            "${diff / SECOND_IN_MILLS} วิ"
        } else {
            "เมื่อสักครู่"
        }
    }
}
