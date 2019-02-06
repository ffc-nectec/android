package ffc.app.util

import java.security.MessageDigest

/**
 * From
 * https://github.com/kittinunf/Fuse/blob/master/fuse/src/main/kotlin/com/github/kittinunf/fuse/util/MD5.kt
 */
fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digested = md.digest(toByteArray())
    return digested.joinToString("") {
        String.format("%02x", it)
    }
}
