package ffc.app

val isDev: Boolean
    get() = BuildConfig.BUILD_TYPE == "debug"

inline fun dev(action: () -> Unit) {
    if (isDev) action()
}
