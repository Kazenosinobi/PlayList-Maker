package com.practicum.playlistmaker.media.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.media.domain.api.MediaRepository
import com.practicum.playlistmaker.media.domain.model.PlayerState
import com.practicum.playlistmaker.utils.InternetConnectionValidator

class MediaRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val internetConnectionValidator: InternetConnectionValidator,
) : MediaRepository {

    override fun preparePlayer(url: String, callback: (PlayerState) -> Unit) {
        if (internetConnectionValidator.isConnected().not()) {
            callback.invoke(PlayerState.STATE_CONNECTION_ERROR)
            return
        }

        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            callback.invoke(PlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            callback.invoke(PlayerState.STATE_PREPARED)
            mediaPlayer.seekTo(START_POSITION)
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
        mediaPlayer.stop()
        mediaPlayer.setOnPreparedListener(null)
        mediaPlayer.setOnCompletionListener(null)
        mediaPlayer.release()
    }

    private companion object {
        private const val START_POSITION = 0
    }
}