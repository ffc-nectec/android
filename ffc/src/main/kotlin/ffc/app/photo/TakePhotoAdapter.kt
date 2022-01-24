package ffc.app.photo

//import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ffc.android.inflate
import ffc.android.onClick
import ffc.android.onLongClick
import ffc.app.R
import ffc.app.util.AdapterClickListener
import kotlinx.android.synthetic.main.item_photo_take.view.delBtn
import kotlinx.android.synthetic.main.item_photo_take.view.image

internal class TakePhotoAdapter(
    var photoUrl: List<Photo>,
    block: AdapterClickListener<Photo>.() -> Unit
) : RecyclerView.Adapter<TakePhotoAdapter.TakePhotoHolder>() {

    private val listener = AdapterClickListener<Photo>()

    init {
        listener.apply(block)
    }

    fun update(photos: List<Photo>) {
        photoUrl = photos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TakePhotoHolder {
        return TakePhotoHolder(parent.inflate(R.layout.item_photo_take), listener)
    }

    override fun getItemCount() = photoUrl.size

    override fun onBindViewHolder(holder: TakePhotoHolder, position: Int) {
        holder.bind(photoUrl[position])
    }

    internal class TakePhotoHolder(
        view: View,
        private val listener: AdapterClickListener<Photo>
    ) : RecyclerView.ViewHolder(view) {

        fun bind(photo: Photo) {
            with(itemView) {
                photo.showOn(image)
                onClick { listener.onItemClick?.invoke(itemView, photo) }
                onLongClick { listener.onItemLongClick?.invoke(itemView, photo) ?: false }
                image.onClick { listener.onViewClick?.invoke(itemView, it, photo) }
                delBtn.onClick { listener.onViewClick?.invoke(itemView, it, photo) }
            }
        }
    }
}
