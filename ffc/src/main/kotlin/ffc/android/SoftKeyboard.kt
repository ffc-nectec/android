package ffc.android

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * from https://stackoverflow.com/a/17789187
 */
fun Activity?.hideSoftKeyboard() {
    if (this == null)
        return
    //Find the currently focused view, so we can grab the correct window token from it.
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    val view = currentFocus ?: View(this)
    view.hideSoftKeyboard()
}

fun View?.hideSoftKeyboard() {
    if (this == null) return
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0);
}
