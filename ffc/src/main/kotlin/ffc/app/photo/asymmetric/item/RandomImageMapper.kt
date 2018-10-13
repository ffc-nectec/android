package ffc.app.photo.asymmetric.item

import ffc.app.photo.asymmetric.ItemImage

internal class RandomImageMapper(val urls: List<String>) : ItemMapper {

    private var _item = listOf<ItemImage>()
    private var isCol2Avail = false

    override val item: List<ItemImage>
        get() = _item

    override val requestColumns: Int
        get() = 3

    init {
        _item = urls.map { parse(it) }
    }

    fun parse(urls: String): ItemImage {
        val i1 = ItemImage(1, urls, urls)
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
