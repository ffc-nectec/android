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

class MinuteAgoPrinter implements TimePrettyPrinter {
    private final CurrentTimer currentTimer;

    public MinuteAgoPrinter(CurrentTimer currentTimer) {
        this.currentTimer = currentTimer;
    }

    @Override
    public String print(long referenceTime) {
        long currentTimeInMills = currentTimer.getInMills();
        long diff = currentTimeInMills - referenceTime;
        return diff / MINITE_IN_MILLS + " นาทีที่แล้ว";
    }
}
