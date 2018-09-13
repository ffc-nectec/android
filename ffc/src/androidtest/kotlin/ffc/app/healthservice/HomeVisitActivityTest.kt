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

package ffc.app.healthservice

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import ffc.app.R
import ffc.entity.healthcare.BloodPressure
import ffc.entity.healthcare.HomeVisit
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeVisitActivityTest {

    @JvmField
    @Rule
    val testRule = ActivityTestRule<HomeVisitActivity>(HomeVisitActivity::class.java)

    @Test
    fun vitalSign() {
        val robot = Robot(testRule)

        robot.testBloodPressure(330, 40)
        robot.testBloodPressure(320, 50)
        robot.testBloodPressure(331, 41, false)
    }

    @Test
    fun bindData() {
        val activity = testRule.activity
    }

    class Robot(val testRule: ActivityTestRule<HomeVisitActivity>) {

        fun testBloodPressure(sys: Int, dia: Int, equal: Boolean = true) {
            onView(withId(R.id.bpSysField)).perform(typeText("$sys"))
            onView(withId(R.id.bpDiaField)).perform(typeText("$dia"))

            with(testRule.activity) {
                val visit = HomeVisit(providerId, personId, notDefineCommunityService)
                this.vitalSign.dataInto(visit)

                kotlin.with(visit) {
                    if (equal)
                        bloodPressure shouldEqual BloodPressure(sys.toDouble(), dia.toDouble())
                    else
                        bloodPressure shouldBe null
                }

                onView(withId(R.id.bpSysField)).perform(clearText())
                onView(withId(R.id.bpDiaField)).perform(clearText())
            }
        }
    }
}
