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

