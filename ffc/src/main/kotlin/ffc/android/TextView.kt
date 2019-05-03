/*
 * Copyright (c) 2019 NECTEC
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

@file:Suppress("DEPRECATION")

package ffc.android

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

fun TextView.getDouble(default: Double? = null): Double? {
    val text = text.toString()
    if (text.isBlank()) return default
    return text.toDouble()
}

@Deprecated("Consider replace with drawableStart to better support right-to-left Layout",
    ReplaceWith("drawableStart"),
    DeprecationLevel.WARNING)
var TextView.drawableLeft: Drawable?
    get() = compoundDrawables[0]
    set(drawable) = setCompoundDrawablesWithIntrinsicBounds(drawable, drawableTop, drawableRight, drawableBottom)

var TextView.drawableStart: Drawable?
    get() = compoundDrawablesRelative[0]
    set(drawable) = compoundDrawablesRelativeWithIntrinsicBounds(start = drawable)

var TextView.drawableTop: Drawable?
    get() = compoundDrawablesRelative[1]
    set(drawable) = compoundDrawablesRelativeWithIntrinsicBounds(top = drawable)

@Deprecated("Consider replace with drawableEnd to better support right-to-left Layout",
    ReplaceWith("drawableEnd"),
    DeprecationLevel.WARNING)
var TextView.drawableRight: Drawable?
    get() = compoundDrawables[2]
    set(drawable) = setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawable, drawableBottom)

var TextView.drawableEnd: Drawable?
    get() = compoundDrawablesRelative[2]
    set(drawable) = compoundDrawablesRelativeWithIntrinsicBounds(end = drawable)

var TextView.drawableBottom: Drawable?
    get() = compoundDrawablesRelative[3]
    set(drawable) = compoundDrawablesRelativeWithIntrinsicBounds(bottom = drawable)

private fun TextView.compoundDrawablesRelativeWithIntrinsicBounds(
    start: Drawable? = drawableStart,
    top: Drawable? = drawableTop,
    end: Drawable? = drawableEnd,
    bottom: Drawable? = drawableBottom
) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
}

class TextWatcherDsl : TextWatcher {
    private var afterChanged: ((Editable?) -> Unit)? = null
    private var beforeChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null
    private var onChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    override fun afterTextChanged(s: Editable?) {
        afterChanged?.invoke(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onChanged?.invoke(s, start, before, count)
    }

    fun afterTextChanged(block: (s: Editable?) -> Unit) {
        afterChanged = block
    }

    fun beforeTextChanged(block: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit) {
        beforeChanged = block
    }

    fun onTextChanged(block: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit) {
        onChanged = block
    }
}

fun TextView.addTextWatcher(block: TextWatcherDsl.() -> Unit) {
    addTextChangedListener(TextWatcherDsl().apply(block))
}
