package ffc.app

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.jetbrains.anko.support.v4.startActivity

class AboutActivity : FamilyFolderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.contentContainer, AboutPreferenceFragment())
            .commit()
    }

    class AboutPreferenceFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_about, rootKey)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            findPreference("version").summary = BuildConfig.VERSION_NAME
            findPreference("license").setOnPreferenceClickListener {
                startActivity<OssLicensesMenuActivity>()
                true
            }
        }
    }
}
