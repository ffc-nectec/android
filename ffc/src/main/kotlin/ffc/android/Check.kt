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

package ffc.android

import android.view.View
import android.widget.TextView

class ViewValidateDsl<T : View> {

    var condition: (T.() -> Boolean)? = null
    lateinit var message: String
    lateinit var validate: T.() -> Boolean

    fun on(condition: T.() -> Boolean) {
        this.condition = condition
    }

    fun that(validate: T.() -> Boolean) {
        this.validate = validate
    }
}

fun <T : View> T.check(block: ViewValidateDsl<T>.() -> Unit) {
    val dsl = ViewValidateDsl<T>().apply(block)
    error(null)
    if (dsl.condition == null || dsl.condition?.invoke(this) == true) {
        val valid = dsl.validate(this)
        if (!valid) {
            error(dsl.message)
            throw IllegalStateException(dsl.message)
        }
    }
}

fun View.error(message: String?) {
    if (textInputLayout != null) { //textInputLayout from View.kt
        textInputLayout?.error = message
        textInputLayout?.isErrorEnabled = !message.isNullOrBlank()
    } else if (this is TextView)
        error = message
}

