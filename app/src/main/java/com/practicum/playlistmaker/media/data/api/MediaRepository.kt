package com.practicum.playlistmaker.media.data.api

import com.practicum.playlistmaker.media.domain.model.PlayerState

interface MediaRepository {
    fun preparePlayer(url: String, callback: (PlayerState) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun getRelease()
}