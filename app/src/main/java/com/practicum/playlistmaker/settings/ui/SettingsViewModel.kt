package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {


    fun share() {
        sharingInteractor.share()
    }

    fun support() {
        sharingInteractor.support()
    }

    fun termsOfUse() {
        sharingInteractor.termsOfUse()
    }

    fun updateTheme(isChecked: Boolean) {
        settingsInteractor.updateTheme(isChecked)
    }

    fun isDarkTheme(): Boolean {
        return settingsInteractor.isDarkTheme()
    }
}