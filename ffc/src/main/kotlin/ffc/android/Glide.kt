package ffc.android

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.load(uri: Uri, requestOptions: RequestOptions.() -> Unit = {}) {
    Glide.with(this)
        .load(uri)
        .apply(RequestOptions().apply(requestOptions))
        .into(this)
}
