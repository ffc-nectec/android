package ffc.android

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import org.jetbrains.anko.dip

fun ImageView.load(uri: Uri, requestOptions: RequestOptions.() -> Unit = {}) {
    Glide.with(this)
        .load(uri)
        .thumbnail(/*sizeMultiplier=*/ 0.25f)
        .apply(RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(false)
            .apply(requestOptions))
        .into(this)
}

fun TextView.loadDrawableBottom(uri: Uri, requestOptions: RequestOptions.() -> Unit = {}) {
    Glide.with(this)
        .load(uri)
        .apply(RequestOptions.circleCropTransform())
        .apply(RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(false)
            .apply(requestOptions))
        .into(object : SimpleTarget<Drawable>(dip(24), dip(24)) {

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                this@loadDrawableBottom.drawableBottom = resource
            }
        })
}
