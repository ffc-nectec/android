package ffc.android

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View

fun Context.color(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)
fun View.color(@ColorRes resId: Int) = ContextCompat.getColor(context, resId)
