package ffc.android

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

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
