package ffc.app.photo.asymmetric.item

import ffc.app.photo.asymmetric.ItemImage

internal class TrippleImageMapper(urls: List<String>) : ItemMapper {

    private var _items = listOf<ItemImage>()

    init {
        require(urls.size % 3 == 0)

        _items = urls.mapIndexed { index, it ->
            ItemImage(index, it, it).apply {
                columnSpan = if(index == 0) 4 else 2
                rowSpan = if(index == 0) 6 else 3
            }
        }
    }

    override val item: List<ItemImage>
        get() = _items
    override val requestColumns: Int
        get() = 6
}
