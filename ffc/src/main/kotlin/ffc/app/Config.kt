package ffc.app

const val isDev = BuildConfig.BUILD_TYPE == "debug"

const val mockRepository = false

inline fun dev(action: () -> Unit) {
    if (isDev) action()
}
