package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.media.data.api.MediaRepository
import com.practicum.playlistmaker.media.domain.model.PlayerState

class MediaInteractorImpl(private val repository: MediaRepository) : MediaInteractor {

    override fun preparePlayer(url: String, callback: (PlayerState) -> Unit) {
        repository.preparePlayer(url, callback)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun getRelease() {
        repository.getRelease()
    }
}