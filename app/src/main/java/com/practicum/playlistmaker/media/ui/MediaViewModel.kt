package com.practicum.playlistmaker.media.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.media.domain.model.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private var timerJob: Job? = null

    fun preparePlayer(url: String) {
        if (url.isBlank()) return
        mediaInteractor.preparePlayer(url) { state ->
            if (state == PlayerState.STATE_PREPARED) {
                //timerJob?.cancel()
                playStatusLiveData.value = PlayerState.STATE_PREPARED
            }
        }
        Log.d("My Log", "preparePlayer")
    }

    private fun startPlayer() {
        mediaInteractor.startPlayer()
        playStatusLiveData.value = PlayerState.STATE_PLAYING
        createUpdateTimerTask()
    }

    fun pausePlayer() {
        if (playStatusLiveData.value != PlayerState.STATE_PLAYING) return
        mediaInteractor.pausePlayer()
        playStatusLiveData.value = PlayerState.STATE_PAUSED
        timerJob?.cancel()
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

    private fun createUpdateTimerTask() {
        timerJob = viewModelScope.launch {
            while (playStatusLiveData.value == PlayerState.STATE_PLAYING) {
                val currentPosition = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaInteractor.getCurrentPosition())
                currentPositionLiveData.value = currentPosition
                delay(PLAY_TIME_DELAY)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        mediaInteractor.getRelease()
        Log.d("My Log", "onCleared: ")
    }

    companion object {
        private const val PLAY_TIME_DELAY = 500L
        private const val DEFAULT_CURRENT_POS = "00:00"
    }
}