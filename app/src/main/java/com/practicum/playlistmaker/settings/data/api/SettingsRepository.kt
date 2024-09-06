package com.practicum.playlistmaker.settings.data.api

interface SettingsRepository {
    fun updateTheme(isChecked: Boolean)
    fun isDarkTheme(): Boolean
}