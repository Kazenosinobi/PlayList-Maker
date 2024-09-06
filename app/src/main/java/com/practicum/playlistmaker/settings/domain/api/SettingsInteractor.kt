package com.practicum.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun updateTheme(isChecked: Boolean)
    fun isDarkTheme(): Boolean
}