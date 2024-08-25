package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun share() {
        repository.share()
    }

    override fun support() {
        repository.support()
    }

    override fun termsOfUse() {
        repository.termsOfUse()
    }
}