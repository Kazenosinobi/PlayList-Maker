package com.practicum.playlistmaker.settings.domain.api

interface SettingsRepository {
    fun updateTheme(isChecked: Boolean)
    fun isDarkTheme(): Boolean
}