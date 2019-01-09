package ffc.app.util.version

import ffc.app.BuildConfig
import ffc.app.util.version.github.GithubVersionChecker

interface VersionChecker {

    fun checkForUpdate(callback: Callback.() -> Unit)

    class Callback {
        var onFail: ((Throwable) -> Unit)? = null
            private set
        var onFound: ((Version) -> Unit)? = null
            private set
        var onNotFound: (() -> Unit)? = null
            private set

        fun onFound(onFound: (Version) -> Unit) {
            this.onFound = onFound
        }

        fun onNotFound(onNotFound: () -> Unit) {
            this.onNotFound = onNotFound
        }

        fun onFail(onFail: (Throwable) -> Unit) {
            this.onFail = onFail
        }
    }
}

fun versionCheck(): VersionChecker {
    val currentVersion = try {
        Version(BuildConfig.VERSION_NAME)
    } catch (ex: Exception) {
        Version("0.0.0")
    }
    return GithubVersionChecker(currentVersion)
}
