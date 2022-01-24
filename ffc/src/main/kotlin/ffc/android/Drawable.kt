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

package ffc.android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
//import android.support.annotation.ColorInt
//import android.support.annotation.DrawableRes
//import android.support.v4.app.Fragment
//import android.support.v4.content.res.ResourcesCompat
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

fun Drawable.tint(@ColorInt color: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN): Drawable {
    return apply { mutate().setColorFilter(color, mode) }
}

fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable && bitmap != null) {
        return bitmap
    }

    val bitmap: Bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    }
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

fun Context.drawable(@DrawableRes drawableRes: Int) = ResourcesCompat.getDrawable(resources, drawableRes, theme)!!
fun Fragment.drawable(@DrawableRes drawableRes: Int) = context!!.drawable(drawableRes)
fun View.drawable(@DrawableRes drawableRes: Int) = context.drawable(drawableRes)
