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

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
//import android.support.annotation.TransitionRes
//import android.support.v4.app.ActivityOptionsCompat
//import android.support.v4.app.Fragment
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.View
import android.view.Window
import android.view.animation.Interpolator
import androidx.annotation.TransitionRes
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment

// import android.support.v4.util.Pair as AndroidSupportPair
import  androidx.core.util.Pair as AndroidSupportPair

fun Context.transition(@TransitionRes res: Int) = TransitionInflater.from(this).inflateTransition(res)
fun Fragment.transition(@TransitionRes res: Int) = context!!.transition(res)
fun View.transition(@TransitionRes res: Int) = context.transition(res)

fun Activity.sceneTransition(): Bundle? {
    return if (Build.VERSION.SDK_INT < 21) null else
        ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
}

fun Activity.sceneTransition(vararg sharedElements: Pair<View, String>?): Bundle? {
    return if (Build.VERSION.SDK_INT < 21) null else
        ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            *sharedElements.map { it?.toAndroidSupportPair() }.toTypedArray()
        ).toBundle()
}

private fun Pair<View, String>.toAndroidSupportPair() = AndroidSupportPair(first, second)

var Fragment.allowTransitionOverlap: Boolean
    set(value) {
        allowEnterTransitionOverlap = value
        allowReturnTransitionOverlap = value
    }
    get() = throw IllegalAccessError()

var Window.allowTransitionOverlap: Boolean
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    set(value) {
        allowEnterTransitionOverlap = value
        allowReturnTransitionOverlap = value
    }
    get() = throw IllegalAccessError()

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Move(context: Context) = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

fun transitionSetOf(vararg transitions: Transition): TransitionSet {
    return TransitionSet().apply {
        transitions.forEach { this.addTransition(it) }
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Transition.excludeSystemView(): Transition {
    excludeTarget(android.R.id.statusBarBackground, true)
    excludeTarget(android.R.id.navigationBarBackground, true)
    return this
}

val enterDuration: Long = 300
val exitDuration: Long = 250
val sharedElementDuration: Long = 250

val sharedElementEasing = androidx.interpolator.view.animation.FastOutSlowInInterpolator()
val enterEasing = androidx.interpolator.view.animation.LinearOutSlowInInterpolator()
val exitEasing = androidx.interpolator.view.animation.FastOutLinearInInterpolator()

fun Transition.shareElement(
    time: Long = sharedElementDuration,
    easing: Interpolator = sharedElementEasing
): Transition {
    duration = time
    interpolator = easing
    return this
}

fun Transition.enter(
    time: Long = enterDuration,
    delay: Long = 0,
    easing: Interpolator = enterEasing
): Transition {
    duration = time
    startDelay = delay
    interpolator = easing
    return this
}

fun Transition.exit(
    time: Long = exitDuration,
    delay: Long = 0,
    easing: Interpolator = exitEasing
): Transition {
    duration = time
    startDelay = delay
    interpolator = easing
    return this
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun Activity.setTransition(action: Window.() -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.action()
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun Fragment.setTransition(action: Fragment.() -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.action()
    }
}
