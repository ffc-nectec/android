/*
 * Copyright (c) 2019 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ffc.app

//import android.support.multidex.MultiDexApplication
//import com.crashlytics.android.Crashlytics
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import ffc.api.FfcCentral
import ffc.app.util.Analytics
import ffc.entity.Lookup
//import io.fabric.sdk.android.Fabric
import me.piruin.spinney.Spinney
import okhttp3.Cache
import timber.log.Timber
import timber.log.Timber.DebugTree

class FamilyFolderApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            //Timber.plant(CrashReportingTree())
        }

        Spinney.setDefaultItemPresenter { item, position ->
            if (item is Lookup) item.name else item.toString()
        }

        FirebaseApp.initializeApp(this)
        Timber.i("Initialized Firebase")
        if (BuildConfig.BUILD_TYPE == "release") {
            Analytics.init(this)
//            val fabric = Fabric.Builder(this)
//                .debuggable(BuildConfig.DEBUG)
//                .kits(Crashlytics())
//                .build()
//            Fabric.with(fabric)
        }

        FfcCentral.cache = Cache(cacheDir, 10 * 1024 * 1024) //10 MB
        FfcCentral.loadUrl(this)
    }

    override fun onTerminate() {
        Analytics.close()
        super.onTerminate()
    }
}
