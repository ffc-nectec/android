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

package ffc.app.util.timeago;

import org.joda.time.DateTime;

class TimePrettyPrinterFactory implements TimePrettyPrinter {
    private final CurrentTimer currentTimer;

    public TimePrettyPrinterFactory(CurrentTimer currentTimer) {
        this.currentTimer = currentTimer;
    }

    @Override
    public String print(long referenceTime) {
        DateTime reference = new DateTime(referenceTime);
        DateTime current = new DateTime(currentTimer.getInMills());

        long diffMills = current.getMillis() - reference.getMillis();
        if (diffMills < MINITE_IN_MILLS)
            return new SecondsAgoPrinter(currentTimer).print(referenceTime);
        else if (diffMills < HOUR_IN_MILLS)
            return new MinuteAgoPrinter(currentTimer).print(referenceTime);
        else if (current.getYear() == reference.getYear() && current.getDayOfYear() - reference.getDayOfYear() == 0)
            return new HoursAgoPrinter(currentTimer).print(referenceTime);
        else if (current.getYear() == reference.getYear() && current.getDayOfYear() - reference.getDayOfYear() == 1)
            return new DaysAgoPrinter(currentTimer).print(referenceTime);
        else
            return new DateTimePrinter(currentTimer).print(referenceTime);

    }
}
