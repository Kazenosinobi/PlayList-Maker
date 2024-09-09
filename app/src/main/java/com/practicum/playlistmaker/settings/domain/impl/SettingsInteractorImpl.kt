package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun updateTheme(isChecked: Boolean) {
        repository.updateTheme(isChecked)
    }

    override fun isDarkTheme(): Boolean {
        return repository.isDarkTheme()
    }
}