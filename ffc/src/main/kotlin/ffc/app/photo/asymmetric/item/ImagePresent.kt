package ffc.app.photo.asymmetric.item

import ffc.app.photo.asymmetric.ImageItem

internal interface ImagePresent {

    val item: List<ImageItem>

    val requestColumns: Int
}

internal fun imageItemPresenterFor(urls: List<String>): ImagePresent = when (urls.size) {
    0 -> ZeroImagePresent()
    1 -> SingleImagePresent(urls)
    2 -> PairImagePresent(urls)
    3 -> TrippleImagePresent(urls)
    else -> FacebookImagePresent(urls)
}
