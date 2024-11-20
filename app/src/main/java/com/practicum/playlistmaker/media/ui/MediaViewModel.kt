package com.practicum.playlistmaker.media.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.media.domain.model.PlayerState
import com.practicum.playlistmaker.mediaLibrary.domain.db.FavouriteTracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaInteractor: MediaInteractor,
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
    track: Track,
) : ViewModel() {

    private val playerStateFlow =
        MutableStateFlow(PlayerStateData())

    fun getPlayerStateFlow() = playerStateFlow.asStateFlow()

    private var timerJob: Job? = null
    private var currentTrack = track

    init {
        observeOnFavourite()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        mediaInteractor.getRelease()
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            when (currentTrack.isFavorite) {
                true -> favouriteTracksInteractor.deleteTrackAtFavouriteTracks(currentTrack)
                false -> favouriteTracksInteractor.addTrackToFavouriteTracks(currentTrack)
            }
            currentTrack = currentTrack.copy(isFavorite = !currentTrack.isFavorite)
            updatePlayerState()
        }
    }

    fun playbackControl(url: String) {
        when (playerStateFlow.value.playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            PlayerState.STATE_DEFAULT, PlayerState.STATE_CONNECTION_ERROR -> {
                preparePlayer(url)
            }
        }
    }

    private fun createUpdateTimerTask() {
        timerJob = viewModelScope.launch {
            while (playerStateFlow.value.playerState == PlayerState.STATE_PLAYING) {
                val position = getCurrentPosition()
                playerStateFlow.update { it.copy(currentPosition = position) }
                delay(PLAY_TIME_DELAY_MILLIS)
            }
        }
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            DATE_FORMAT_PATTERN,
            Locale.getDefault()
        ).format(mediaInteractor.getCurrentPosition())
    }

    fun preparePlayer(url: String) {
        if (url.isBlank()) return
        mediaInteractor.preparePlayer(url) { state ->
            when (state) {
                PlayerState.STATE_PREPARED -> {
                    playerStateFlow.update {
                        it.copy(playerState = PlayerState.STATE_PREPARED)
                    }
                    timerJob?.cancel()
                }

                PlayerState.STATE_CONNECTION_ERROR -> {
                    playerStateFlow.update {
                        it.copy(playerState = PlayerState.STATE_CONNECTION_ERROR)
                    }
                }

                else -> Unit
            }
        }
    }

    fun pausePlayer() {
        if (playerStateFlow.value.playerState != PlayerState.STATE_PLAYING) return
        mediaInteractor.pausePlayer()
        playerStateFlow.update {
            it.copy(playerState = PlayerState.STATE_PAUSED)
        }
        timerJob?.cancel()
    }

    private fun startPlayer() {
        mediaInteractor.startPlayer()
        playerStateFlow.update {
            it.copy(playerState = PlayerState.STATE_PLAYING)
        }
        createUpdateTimerTask()
    }

    private fun updatePlayerState() {
        playerStateFlow.update {
            it.copy(isFavourite = currentTrack.isFavorite)
        }
    }

    private fun observeOnFavourite() {
        favouriteTracksInteractor.isFavourite(currentTrack.trackId)
            .onEach {
                currentTrack = currentTrack.copy(isFavorite = it)
                updatePlayerState()
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        private const val PLAY_TIME_DELAY_MILLIS = 300L
        private const val DATE_FORMAT_PATTERN = "mm:ss"
    }
}