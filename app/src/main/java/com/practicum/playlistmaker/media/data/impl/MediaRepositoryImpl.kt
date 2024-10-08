package com.practicum.playlistmaker.media.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.media.domain.api.MediaRepository
import com.practicum.playlistmaker.media.domain.model.PlayerState

class MediaRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaRepository {

    override fun preparePlayer(url: String, callback: (PlayerState) -> Unit) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            callback.invoke(PlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            callback.invoke(PlayerState.STATE_PREPARED)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition ?: 0
    }

    override fun getRelease() {
        mediaPlayer.stop()
        mediaPlayer.setOnPreparedListener(null)
        mediaPlayer.setOnCompletionListener(null)
        mediaPlayer.release()
    }
}