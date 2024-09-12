package com.practicum.playlistmaker.settings.ui

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.core.App
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