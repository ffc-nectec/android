package ffc.app.util.value

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

//import android.support.annotation.ColorInt
//import android.support.annotation.ColorRes
//import android.support.annotation.DrawableRes

class Value(
    val label: String? = null,
    val value: String = "-",
    val caption: String? = null,
    @ColorInt val color: Int? = null,
    @ColorRes val colorRes: Int? = null,
    @DrawableRes val iconRes: Int? = null
)
