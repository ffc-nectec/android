package ffc.v3

import android.app.Application
import ffc.entity.Lookup
import me.piruin.spinney.Spinney

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Spinney.setDefaultItemPresenter { item, position ->
            if (item is Lookup) item.name else item.toString()
        }
    }
}

