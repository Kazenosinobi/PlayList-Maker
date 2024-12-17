package com.practicum.playlistmaker.core

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.markodevcic.peko.PermissionRequester
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    var darkTheme = false
    private var sharedPrefs: SharedPreferences? = null
    override fun onCreate() {
        super.onCreate()

        PermissionRequester.initialize(applicationContext)

        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, repositoryModule, interactorModule, viewModelModule))
        }

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

    private companion object {
        private const val THEME_SWITCHER_KEY = "key_for_theme_switcher"
        private const val PREFERENCES = "preferences"
    }
}