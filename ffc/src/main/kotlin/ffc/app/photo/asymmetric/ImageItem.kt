package ffc.app.photo.asymmetric

import android.os.Parcel
import android.os.Parcelable
import com.felipecsl.asymmetricgridview.AsymmetricItem

/**
 * Modified from ItemImage at abhisheklunagaria/FacebookTypeImageGrid
 */
class ImageItem(
    var urls: String,
    var _columnSpan: Int = 1,
    var _rowSpan: Int = 1
) : AsymmetricItem
{

    override fun getColumnSpan(): Int = _columnSpan
    override fun getRowSpan(): Int = _rowSpan

    fun setColumnSpan(span: Int) {
        _columnSpan = span
    }

    fun setRowSpan(span: Int) {
        _rowSpan = span
    }

    protected constructor(parcel: Parcel) : this(
        urls = parcel.readString()!!,
        _columnSpan = parcel.readInt(),
        _rowSpan = parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(urls)
        dest.writeInt(_columnSpan)
        dest.writeInt(_rowSpan)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ImageItem> = object : Parcelable.Creator<ImageItem> {
            override fun createFromParcel(parcel: Parcel): ImageItem {
                return ImageItem(parcel)
            }

            override fun newArray(size: Int): Array<ImageItem> {
                return newArray(size)
            }
        }
    }
}
