package com.practicum.playlistmaker.settings.data.impl

import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val application: App) : SettingsRepository {
    override fun updateTheme(isChecked: Boolean) {
        application.switchTheme(isChecked)
    }

    override fun isDarkTheme(): Boolean {
        return application.darkTheme
    }

}