package ffc.app.util.version.github

import ffc.api.ApiErrorException
import ffc.app.util.version.Version
import ffc.app.util.version.VersionChecker
import ffc.entity.gson.ffcGson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.dsl.enqueue

internal class GithubVersionChecker(val currentVersion: Version) : VersionChecker {

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(ffcGson))
        .baseUrl("https://api.github.com/")
        .build()

    override fun checkForUpdate(callback: VersionChecker.Callback.() -> Unit) {
        val checkerCallback = VersionChecker.Callback().apply(callback)
        retrofit.create(GithubReleaseApi::class.java).latestRelease().enqueue {
            onSuccess {
                val latestRelease = body()
                if (latestRelease == null) {
                    checkerCallback.onFail!!.invoke(IllegalArgumentException("Release body is empty"))
                    return@onSuccess
                }
                latestRelease.let {
                    val latestVersion = Version(it.tag_name)
                    if (latestVersion > currentVersion)
                        checkerCallback.onFound!!.invoke(latestVersion)
                    else
                        checkerCallback.onNotFound!!.invoke()
                }
            }
            onError { checkerCallback.onFail!!.invoke(ApiErrorException(this)) }
            onFailure { checkerCallback.onFail!!.invoke(it) }
        }
    }
}



