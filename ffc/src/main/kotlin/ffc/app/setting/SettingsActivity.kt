package ffc.app.setting

import android.net.Uri
import android.os.Bundle
import android.provider.Settings
//import android.support.v7.preference.EditTextPreference
//import android.support.v7.preference.Preference
//import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ffc.api.FfcCentral
import ffc.app.BuildConfig
import ffc.app.FamilyFolderActivity
import ffc.app.R
import org.jetbrains.anko.support.v4.toast

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
            setPreferencesFromResource(ffc.app.R.xml.pref_settings, rootKey)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            preferenceScreen.sharedPreferences.edit().putString("api_url", FfcCentral.url).apply()

            with(findPreference("api_url")) {
                this as EditTextPreference
                summary = FfcCentral.url
                text = FfcCentral.url
                isVisible = BuildConfig.DEBUG
                setOnPreferenceChangeListener { preference, input ->
                    try {
                        input as String
                        require(input.startsWith("https://")) { "must start with https://" }
                        FfcCentral.saveUrl(requireContext(), Uri.parse(input))
                        preference as EditTextPreference
                        preference.summary = input
                        //toast("updated API address!")
                        Toast.makeText(requireContext(), "updated API address!", Toast.LENGTH_SHORT).show()
                        return@setOnPreferenceChangeListener true
                    } catch (ex: IllegalArgumentException) {
                        Toast.makeText(requireContext(), "Url ${ex.message}", Toast.LENGTH_SHORT).show()
                        return@setOnPreferenceChangeListener false
                    }
                }
            }
        }
    }
}
