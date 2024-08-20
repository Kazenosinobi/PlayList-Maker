package com.practicum.playlistmaker.media.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.media.domain.api.MediaRepository
import com.practicum.playlistmaker.media.domain.model.PlayerState

class MediaRepositoryImpl() : MediaRepository {

    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String, callback: (PlayerState) -> Unit) {
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
        return mediaPlayer.currentPosition
    }

    override fun getRelease() {
        mediaPlayer.release()
    }
}