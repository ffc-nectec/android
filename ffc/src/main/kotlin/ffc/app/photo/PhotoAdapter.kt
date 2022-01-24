package ffc.app.photo

import android.app.Activity
import android.net.Uri
//import android.support.v7.widget.RecyclerView
//import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ffc.android.inflate
import ffc.android.load
import ffc.android.onClick
import ffc.android.sceneTransition
import ffc.app.R
import ffc.app.util.AdapterClickListener
import org.jetbrains.anko.find

internal class PhotoAdapter(
    var photoUrl: List<Uri>,
    block: AdapterClickListener<Uri>.() -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    private val listener = AdapterClickListener<Uri>().apply(block)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder(parent.inflate(R.layout.item_photo), listener)
    }

    override fun getItemCount() = photoUrl.size

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.bind(photoUrl[position])
    }

    class PhotoHolder(view: View, val listener: AdapterClickListener<Uri>) : RecyclerView.ViewHolder(view) {
        val image = view.find<ImageView>(R.id.image)

        fun bind(uri: Uri) {
            image.load(uri)
            image.onClick { listener.onViewClick?.invoke(itemView, it, uri) }
        }
    }
}

val RecyclerView.urls: List<String>?
    get() {
        var url: List<String>? = null
        adapter?.let {
            it as PhotoAdapter
            url = it.photoUrl.map { it.toString() }
        }
        return url
    }

fun RecyclerView.bindPhotoUrl(activity: Activity, urls: List<String>) = bindPhoto(activity, urls.map { Uri.parse(it) })

private fun RecyclerView.bindPhoto(activity: Activity, uris: List<Uri>) {
    val span = if (uris.size > 1) 2 else 1
    layoutManager = StaggeredGridLayoutManager(span, StaggeredGridLayoutManager.VERTICAL)
    adapter = PhotoAdapter(uris) {
        onViewClick { view, uri ->
            activity.viewPhoto(uri, activity.sceneTransition(view to activity.getString(R.string.transition_photo)))
        }
    }
}
