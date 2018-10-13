package ffc.app.photo.asymmetric.item

import ffc.app.photo.asymmetric.ItemImage

internal interface ItemMapper {

    val item: List<ItemImage>

    val requestColumns: Int
}

internal fun itemMapperFor(urls: List<String>): ItemMapper = when (urls.size) {
    1 -> SingleImageMapper(urls)
    2 -> PairImageMapper(urls)
    3 -> TrippleImageMapper(urls)
    in 4..8 -> FacebookImageMapper(urls)
    else -> RandomImageMapper(urls)
}
