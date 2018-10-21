package ffc.app.photo.asymmetric.item

import ffc.app.photo.asymmetric.ImageItem

internal class TrippleImagePresent(urls: List<String>) : ImagePresent {

    private var _items = listOf<ImageItem>()

    init {
        require(urls.size % 3 == 0)

        _items = urls.mapIndexed { index, it ->
            ImageItem(it,
                _columnSpan = if (index == 0) 4 else 2,
                _rowSpan = if (index == 0) 6 else 3
            )
        }
    }

    override val item: List<ImageItem>
        get() = _items
    override val requestColumns: Int
        get() = 6
    override val maxDisplayItem: Int
        get() = 3
}
