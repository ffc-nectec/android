package ffc.app.photo

import android.os.Bundle
import android.transition.Fade
import ffc.android.load
import ffc.android.setTransition
import ffc.app.FamilyFolderActivity
import ffc.app.R
import kotlinx.android.synthetic.main.photo_activity.photo

class PhotoActivity : FamilyFolderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransition {
            enterTransition = Fade()
        }
        setContentView(R.layout.photo_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val uri = intent.data
        photo.load(uri!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
