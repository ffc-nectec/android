package ffc.app.util

/**
 * MIT License
 *
 * Copyright (c) 2018 Piruin Panichphol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

import android.widget.EditText
import java.text.NumberFormat
import kotlin.math.pow

fun Double.format(): String = NumberFormat.getInstance().format(this)

infix fun Double.and(second: Double) = Pair(this, second)

infix fun Double.between(values: Pair<Double, Double>) = values.first <= this && this <= values.second

fun Double.toRadians() = Math.toRadians(this)

val Double.fractional
    get() = this - this.toInt()

val Double.wholeNum
    get() = (this - this.fractional).toInt()

fun Double.round(digitLength: Int): Double {
    val pow = 10.0.pow(digitLength)
    return Math.round(this * pow) / pow
}

fun Double?.setInto(editText: EditText, digitLength: Int = 1) = takeIf { it != null }?.let {
    if (it.fractional == 0.0) {
        editText.setText("${it.wholeNum}")
    } else {
        editText.setText("${it.round(digitLength)}")
    }
}
