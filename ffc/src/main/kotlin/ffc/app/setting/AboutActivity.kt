package ffc.app.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
//import android.support.v7.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import ffc.api.FfcCentral
import ffc.app.BuildConfig
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.auth.legal.LegalAgreementApi
import ffc.app.auth.legal.LegalType
import ffc.app.auth.legal.LegalType.privacy
import ffc.app.auth.legal.LegalType.terms
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.intentFor
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
            //super.onActivityCreated(savedInstanceState)
            findPreference("version").summary = BuildConfig.VERSION_NAME
            findPreference("license").setOnPreferenceClickListener {
                requireContext().startActivity<OssLicensesMenuActivity>()
                true
            }
            findPreference("terms").intent = intentOfLegal(terms)
            findPreference("privacy").intent = intentOfLegal(privacy)
        }

        private fun intentOfLegal(type: LegalType): Intent {
            val term = FfcCentral().service<LegalAgreementApi>().latest(type)
            return  requireContext().intentFor<LegalViewActivity>().apply {
                data = Uri.parse(term.request().url().toString())
            }
        }
    }
}
