package ffc.app.util.version.github

import org.joda.time.DateTime

class GithubRelease(
    val tag_name: String,
    val name: String,
    val body: String,
    val prerelease: Boolean,
    val created_at: DateTime,
    val published_at: DateTime)
