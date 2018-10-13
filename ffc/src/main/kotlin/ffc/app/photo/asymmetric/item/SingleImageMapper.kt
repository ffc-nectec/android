package ffc.app.photo.asymmetric.item

import ffc.app.photo.asymmetric.ItemImage

internal class SingleImageMapper(val urls: List<String>) : ItemMapper {

    private val itemImage = ItemImage(0, urls[0], urls[0])

    override val item: List<ItemImage>
        get() {
            return listOf(itemImage)
        }

    override val requestColumns: Int
        get() = 1
}
