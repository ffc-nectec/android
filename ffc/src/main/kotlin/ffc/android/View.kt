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

//import android.support.annotation.LayoutRes
//import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import com.google.android.material.textfield.TextInputLayout

fun <T : View> T.onClick(listener: (T) -> Unit) = setOnClickListener { listener(this) }

fun <T : View> T.onLongClick(listener: (T) -> Boolean) = setOnLongClickListener { listener(this) }

fun View.requestScroll(focus: Boolean = true) {
    if (focus) requestFocus()
    parent.requestChildFocus(this, this)
}

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}

val EditText.isError
    get() = this.textInputLayout?.error?.isNotBlank() == true || error.isNotBlank()

val View.textInputLayout: TextInputLayout?
    get() {
        if (this.parent == null) return null
        return when {
            this.parent is TextInputLayout -> this.parent as TextInputLayout
            this.parent is View -> (this.parent as View).textInputLayout
            else -> null
        }
    }

fun View.gone() = updateVisibility(View.GONE)

fun View.invisible() = updateVisibility(View.INVISIBLE)

fun View.visible() = updateVisibility(View.VISIBLE)

private fun View.updateVisibility(visibility: Int) {
    when {
        this.textInputLayout != null -> this.textInputLayout!!.visibility = visibility
        else -> this.visibility = visibility
    }
}

val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this.context)

fun ViewGroup.inflate(@LayoutRes resource: Int, attach: Boolean = false) =
    LayoutInflater.from(this.context).inflate(resource, this, attach)

fun notEmpty(vararg editTexts: EditText) = editTexts.none { it.text.isNullOrBlank() }
