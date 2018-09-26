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

import org.joda.time.DateTime

import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

internal class DateTimePrinter(private val currentTimer: CurrentTimer) : TimePrettyPrinter {

    override fun print(referenceTime: Long): String {
        val currentTimeInMills = DateTime(currentTimer.inMills)
        val agoDateTime = DateTime(referenceTime)

        return if (currentTimeInMills.year == agoDateTime.year)
            String.format(Locale.US, "%d %s %02d:%02d",
                    agoDateTime.getDayOfMonth(),
                    monthNameMap.get(agoDateTime.getMonthOfYear()),
                    agoDateTime.getHourOfDay(),
                    agoDateTime.getMinuteOfHour())
        else {
            String.format(Locale.US, "%d %s %04d %02d:%02d",
                    agoDateTime.getDayOfMonth(),
                    monthNameMap.get(agoDateTime.getMonthOfYear()),
                    agoDateTime.getYear() + 543,
                    agoDateTime.getHourOfDay(),
                    agoDateTime.getMinuteOfHour()
            )
        }
    }

    companion object {
        private val monthNameMap = ConcurrentHashMap<Int, String>()

        init {
            monthNameMap.put(1, "ม.ค.")
            monthNameMap.put(2, "ก.พ.")
            monthNameMap.put(3, "มี.ค.")
            monthNameMap.put(4, "เม.ย.")
            monthNameMap.put(5, "พ.ค.")
            monthNameMap.put(6, "มิ.ย.")
            monthNameMap.put(7, "ก.ค.")
            monthNameMap.put(8, "ส.ค.")
            monthNameMap.put(9, "ก.ย.")
            monthNameMap.put(10, "ต.ค.")
            monthNameMap.put(11, "พ.ย.")
            monthNameMap.put(12, "ธ.ค.")
        }
    }
}
