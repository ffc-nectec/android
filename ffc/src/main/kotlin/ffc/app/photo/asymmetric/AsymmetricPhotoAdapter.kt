package ffc.app.photo.asymmetric

import android.app.Activity
import android.net.Uri
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.felipecsl.asymmetricgridview.AGVRecyclerViewAdapter
import com.felipecsl.asymmetricgridview.AsymmetricItem
import com.felipecsl.asymmetricgridview.AsymmetricRecyclerView
import com.felipecsl.asymmetricgridview.AsymmetricRecyclerViewAdapter
import ffc.android.SpacesItemDecoration
import ffc.android.load
import ffc.android.sceneTransition
import ffc.app.R
import ffc.app.photo.asymmetric.item.imageItemPresenterFor
import ffc.app.photo.viewPhoto
import ffc.app.util.AdapterClickListener
import org.jetbrains.anko.dip

/**
 * Modified from ChildAdapter at abhisheklunagaria/FacebookTypeImageGrid
 */
internal class AsymmetricPhotoAdapter(
    private val items: List<ImageItem>,
    private val maxDisplay: Int = 4,
    private val onPhotoClickDsl: AdapterClickListener<Uri>.() -> Unit = {}
) : AGVRecyclerViewAdapter<ViewHolder>()
{

    private val listener = AdapterClickListener<Uri>().apply(onPhotoClickDsl)

    private val displayItem: Int = if (items.size >= maxDisplay) maxDisplay else items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items, position, displayItem, items.size)
    }

    override fun getItemCount(): Int {
        return displayItem
    }

    override fun getItem(position: Int): AsymmetricItem {
        return items[position]
    }
}

internal class ViewHolder(view: View, val listener: AdapterClickListener<Uri>) : RecyclerView.ViewHolder(view) {

    private val imageView: ImageView = itemView.findViewById(R.id.mImageView)
    private val textView: TextView = itemView.findViewById(R.id.tvCount)

    fun bind(item: List<ImageItem>, position: Int, mDisplay: Int, mTotal: Int) {
        val uri = Uri.parse(item.get(position).urls)
        imageView.load(uri)
        textView.text = "+" + (mTotal - mDisplay)
        if (mTotal > mDisplay) {
            if (position == mDisplay - 1) {
                textView.visibility = View.VISIBLE
                imageView.setAlpha(72)
            } else {
                textView.visibility = View.GONE
                imageView.setAlpha(255)
            }
        } else {
            imageView.setAlpha(255)
            textView.visibility = View.GONE
        }
        listener.bindOnViewClick(itemView, uri, imageView)
    }
}

fun AsymmetricRecyclerView.bind(urls: List<String>) {
    val presenter = imageItemPresenterFor(urls)
    setRequestedColumnCount(presenter.requestColumns)
    requestedHorizontalSpacing = dip(3)
    addItemDecoration(SpacesItemDecoration(dip(3)))
    val itemAdapter = AsymmetricPhotoAdapter(presenter.item, presenter.maxDisplayItem) {
        onViewClick { view, uri ->
            val activity = context as Activity
            activity.viewPhoto(uri, activity.sceneTransition(view to activity.getString(R.string.transition_photo)))
        }
    }
    adapter = AsymmetricRecyclerViewAdapter(context, this, itemAdapter)
}
