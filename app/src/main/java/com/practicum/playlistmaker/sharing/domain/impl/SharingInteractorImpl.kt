package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.SharingRepository
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val repository: SharingRepository) : SharingInteractor{
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