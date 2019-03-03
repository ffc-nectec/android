package ffc.app.util

inline fun <T> Iterable<T>.forEachChunk(
    size: Int,
    delay: Long = 0,
    action: (progress: Double, list: List<T>) -> Unit
) {
    val chunked = chunked(size)
    chunked.forEachIndexed { index, list ->
        Thread.sleep(delay)
        action(index / chunked.size.toDouble(), list)
    }
}
