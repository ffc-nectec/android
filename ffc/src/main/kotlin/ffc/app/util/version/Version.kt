package ffc.app.util.version

import java.util.regex.Pattern

class Version(name: String) : Comparable<Version> {
    val major: Int
    val minor: Int
    val patch: Int

    init {
        val pattern = Pattern.compile("^(\\d+\\.)(\\d+\\.)(\\*|\\d+)(.*)")
        val matcher = pattern.matcher(name.trim())

        if (matcher.matches()) {
            major = Integer.parseInt(matcher.group(1).replace(".", ""))
            minor = Integer.parseInt(matcher.group(2).replace(".", ""))
            patch = Integer.parseInt(matcher.group(3))
        } else {
            throw IllegalArgumentException("version name not match pattern $name")
        }
    }

    override fun compareTo(other: Version): Int {
        val major = Integer.compare(this.major, other.major)
        if (major != 0)
            return major
        val cMinor = Integer.compare(minor, other.minor)
        return if (cMinor != 0) cMinor else Integer.compare(patch, other.patch)
    }

    override fun toString(): String {
        return major.toString() + "." + minor + "." + patch
    }
}
