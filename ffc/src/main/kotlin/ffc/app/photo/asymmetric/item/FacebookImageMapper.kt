package ffc.app.photo.asymmetric.item

import android.view.Gravity
import ffc.app.photo.asymmetric.ItemImage

internal class FacebookImageMapper(
    urls: List<String>,
    val gravity: Int = if (urls.hashCode() % 2 == 0) Gravity.TOP else Gravity.START
) : ItemMapper {

    private var _items = listOf<ItemImage>()

    init {
        require(urls.size >= 4)

        _items = urls.mapIndexed { index, it ->
            ItemImage(index, it, it).apply {
                when (gravity) {
                    Gravity.TOP -> {
                        columnSpan = if (index == 0) 3 else 1
                        rowSpan = if (index == 0) 2 else 1
                    }
                    else -> {
                        columnSpan = if (index == 0) 2 else 1
                        rowSpan = if (index == 0) 3 else 1
                    }
                }
            }
        }
    }

    override val item: List<ItemImage>
        get() = _items

    override val requestColumns: Int
        get() = 3
}
