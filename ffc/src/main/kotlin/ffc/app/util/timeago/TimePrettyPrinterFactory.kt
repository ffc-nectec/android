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

internal class TimePrettyPrinterFactory(private val currentTimer: CurrentTimer) : TimePrettyPrinter {

    override fun print(referenceTime: Long): String {
        val reference = DateTime(referenceTime)
        val current = DateTime(currentTimer.inMills)

        val diffMills = current.getMillis() - reference.getMillis()
        return if (diffMills < MINITE_IN_MILLS)
            SecondsAgoPrinter(currentTimer).print(referenceTime)
        else if (diffMills < HOUR_IN_MILLS)
            MinuteAgoPrinter(currentTimer).print(referenceTime)
        else if (current.year == reference.year && current.dayOfYear - reference.dayOfYear == 0)
            HoursAgoPrinter(currentTimer).print(referenceTime)
        else if (current.year == reference.year && current.dayOfYear - reference.dayOfYear == 1)
            DaysAgoPrinter(currentTimer).print(referenceTime)
        else
            DateTimePrinter(currentTimer).print(referenceTime)
    }
}
