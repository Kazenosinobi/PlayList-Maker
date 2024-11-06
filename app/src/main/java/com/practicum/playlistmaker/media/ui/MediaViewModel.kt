package com.practicum.playlistmaker.media.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.media.domain.model.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaInteractor: MediaInteractor,
) : ViewModel() {

    private val playStatusStateFlow = MutableStateFlow(PlayerState.STATE_DEFAULT)
    private val currentPositionStateFlow = MutableStateFlow(DEFAULT_CURRENT_POS)

    fun getPlayStatusStateFlow() = playStatusStateFlow.asStateFlow()
    fun getCurrentPositionStateFlow() = currentPositionStateFlow.asStateFlow()

    private var timerJob: Job? = null

    fun preparePlayer(url: String) {
        if (url.isBlank()) return
        mediaInteractor.preparePlayer(url) { state ->
            if (state == PlayerState.STATE_PREPARED) {
                playStatusStateFlow.value = PlayerState.STATE_PREPARED
                timerJob?.cancel()
            }
        }
    }

    private fun startPlayer() {
        mediaInteractor.startPlayer()
        playStatusStateFlow.value = PlayerState.STATE_PLAYING
        createUpdateTimerTask()
    }

    fun pausePlayer() {
        if (playStatusStateFlow.value != PlayerState.STATE_PLAYING) return
        mediaInteractor.pausePlayer()
        playStatusStateFlow.value = PlayerState.STATE_PAUSED
        timerJob?.cancel()
    }

    fun playbackControl(url: String) {
        when (playStatusStateFlow.value) {
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
            while (playStatusStateFlow.value == PlayerState.STATE_PLAYING) {
                val position = getCurrentPosition()
                currentPositionStateFlow.value = position
                delay(PLAY_TIME_DELAY)
            }
        }
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaInteractor.getCurrentPosition())
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        mediaInteractor.getRelease()
    }

    companion object {
        private const val PLAY_TIME_DELAY = 300L
        private const val DEFAULT_CURRENT_POS = "00:00"
    }
}