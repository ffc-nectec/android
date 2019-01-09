package ffc.app.util.version.github

import retrofit2.Call
import retrofit2.http.GET

interface GithubReleaseApi {

    @GET("repos/ffc-nectec/android/releases/latest")
    fun latestRelease(): Call<GithubRelease>
}
