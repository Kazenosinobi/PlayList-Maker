package com.practicum.playlistmaker.media

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.core.Creator
import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.media.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaInteractor: MediaInteractor,
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())

    private val playStatusLiveData = MutableLiveData(PlayerState.STATE_DEFAULT)
    private val currentPositionLiveData = MutableLiveData(DEFAULT_CURRENT_POS)

    fun getPlayStatusLiveData(): LiveData<PlayerState> = playStatusLiveData
    fun getCurrentPositionLiveData(): LiveData<String> = currentPositionLiveData

    fun preparePlayer(url: String) {
        if (url.isBlank()) return
        mediaInteractor.preparePlayer(url) { state ->
            if (state == PlayerState.STATE_PREPARED) {
                handler.removeCallbacks(createUpdateTimerTask())
                playStatusLiveData.value = PlayerState.STATE_PREPARED
            }
        }
    }

    private fun startPlayer() {
        mediaInteractor.startPlayer()
        playStatusLiveData.value = PlayerState.STATE_PLAYING
        handler.post(createUpdateTimerTask())
    }

    fun pausePlayer() {
        if (playStatusLiveData.value != PlayerState.STATE_PLAYING) return
        mediaInteractor.pausePlayer()
        playStatusLiveData.value = PlayerState.STATE_PAUSED
        handler.removeCallbacks(createUpdateTimerTask())
    }

    fun playbackControl(url: String) {
        when (playStatusLiveData.value) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            PlayerState.STATE_DEFAULT -> {
                preparePlayer(url)
            }

            null -> Unit
        }
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playStatusLiveData.value == PlayerState.STATE_PLAYING) {
                    val currentPosition = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaInteractor.getCurrentPosition())
                    currentPositionLiveData.value = currentPosition
                    handler.postDelayed(this, PLAY_TIME_DELAY)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(createUpdateTimerTask())
    }

    companion object {
        private const val PLAY_TIME_DELAY = 500L
        private const val DEFAULT_CURRENT_POS = "00:00"

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = Creator.provideMediaInteractor()

                MediaViewModel(
                    interactor
                )
            }
        }
    }
}