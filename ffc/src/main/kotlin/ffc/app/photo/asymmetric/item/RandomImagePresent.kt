package ffc.app.photo.asymmetric.item

import ffc.app.photo.asymmetric.ImageItem

/**
 * Modified from DeviceMainActivity.java at abhisheklunagaria/FacebookTypeImageGrid
 */
internal class RandomImagePresent(val urls: List<String>) : ImagePresent {

    private var _item = listOf<ImageItem>()
    private var isCol2Avail = false

    override val item: List<ImageItem>
        get() = _item
    override val requestColumns: Int
        get() = 3
    override val maxDisplayItem: Int
        get() = 5

    init {
        _item = urls.map { parse(it) }
    }

    fun parse(urls: String): ImageItem {
        val i1 = ImageItem( urls)
        var colSpan = if (Math.random() < 0.2f) 2 else 1
        val rowSpan = colSpan
        if (colSpan == 2 && !isCol2Avail)
            isCol2Avail = true
        else if (colSpan == 2 && isCol2Avail)
            colSpan = 1
        i1.columnSpan = colSpan
        i1.rowSpan = rowSpan
        return i1
    }
}
