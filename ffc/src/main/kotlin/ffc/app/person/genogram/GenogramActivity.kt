package ffc.app.person.genogram

import android.os.Bundle
import android.os.Handler
import com.otaliastudios.zoom.ZoomLayout
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.person.personId
import ffc.app.util.alert.handle
import ffc.genogram.android.GenogramView
import ffc.entity.Person as FFCPerson

class GenogramActivity : FamilyFolderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genogram)

        val personId = intent.personId

        val families = ApiFamilies(org!!, personId!!)
        families.family {
            onSuccess {
                val view = GenogramView(this@GenogramActivity)
                view.nodeBuilder = PersonNodeBuilder()
                view.drawFamily(it)

                val container = findViewById<ZoomLayout>(R.id.container)
                container.addView(view)
                Handler().postDelayed({ container.zoomOut() }, 150)
            }
            onFail { handle(it!!) }
        }
    }
}
