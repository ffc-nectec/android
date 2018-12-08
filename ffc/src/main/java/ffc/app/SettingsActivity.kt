package ffc.app

import android.os.Bundle
import android.provider.Settings
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import java.lang.IllegalArgumentException

class SettingsActivity : FamilyFolderActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.contentContainer, SettingsFragment())
            .commit()
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        val fragment = findPreferenceFragmentByName(pref.fragment)
        fragment.arguments = pref.extras
        fragment.setTargetFragment(caller, 0)
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragment)
            .addToBackStack(null)
            .commit()
        Settings.ACTION_DISPLAY_SETTINGS
        return true
    }

    fun findPreferenceFragmentByName(name: String): PreferenceFragmentCompat {
        return when (name) {
            AboutActivity.AboutPreferenceFragment::class.java.name -> AboutActivity.AboutPreferenceFragment()
            else -> throw IllegalArgumentException("Not found fragment")
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_settings, rootKey)
        }
    }
}
