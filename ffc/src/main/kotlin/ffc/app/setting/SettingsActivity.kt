package ffc.app.setting

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
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

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

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
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            when (key) {
                "api_url" -> {
                    toast("updated API address!")
                    val newValue = sharedPreferences!!.getString(key, null)
                    findPreference("api_url").summary = newValue
                    FfcCentral.saveUrl(context!!, Uri.parse(newValue!!))
                }
            }
        }
    }
}
