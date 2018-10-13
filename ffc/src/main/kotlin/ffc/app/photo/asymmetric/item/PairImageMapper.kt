package ffc.app.photo.asymmetric.item

import ffc.app.photo.asymmetric.ItemImage

internal class PairImageMapper(urls: List<String>) : ItemMapper {

    private var _items = listOf<ItemImage>()

    init {
        require(urls.size % 2 == 0)

        _items = urls.map {
            ItemImage(0, it, it).apply {
                columnSpan = 2
                rowSpan = 3
            }
        }
    }

    override val item: List<ItemImage>
        get() = _items
    override val requestColumns: Int
        get() = 4
}
