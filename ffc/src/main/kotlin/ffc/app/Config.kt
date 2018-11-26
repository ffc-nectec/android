package ffc.app

val isDev: Boolean
    get() = BuildConfig.BUILD_TYPE == "debug" && true

inline fun dev(action: () -> Unit) {
    if (isDev) action()
}
