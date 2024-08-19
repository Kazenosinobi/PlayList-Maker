package com.practicum.playlistmaker.data.network

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    var darkTheme = false
    private var sharedPrefs: SharedPreferences? = null
    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs?.getBoolean(THEME_SWITCHER_KEY, false) ?: false
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveTheme(darkTheme)
    }

    private fun saveTheme(darkTheme: Boolean) {
        sharedPrefs
            ?.edit()
            ?.putBoolean(THEME_SWITCHER_KEY, darkTheme)
            ?.apply()
    }

    companion object {
        const val THEME_SWITCHER_KEY = "key_for_theme_switcher"
        const val PREFERENCES = "preferences"
    }
}