package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.media.domain.model.PlayerState

interface MediaInteractor {
    fun preparePlayer(url: String, callback: (PlayerState) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun getRelease()
}