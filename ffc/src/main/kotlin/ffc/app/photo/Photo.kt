package ffc.app.photo

import android.net.Uri
import android.widget.ImageView
import ffc.android.load

interface Photo {
    val uri: Uri

    fun showOn(imageView: ImageView) {
        imageView.load(uri)
    }
}

internal class UrlPhoto(val url: String) : Photo {
    override val uri: Uri get() = Uri.parse(url)
}

internal class UriPhoto(override val uri: Uri) : Photo
