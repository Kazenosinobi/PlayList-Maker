package com.practicum.playlistmaker.media

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.media.data.model.TrackPlayer
import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.media.domain.model.PlayerState
import com.practicum.playlistmaker.media.ui.MediaActivity.Companion.START_TIME
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaInteractor: MediaInteractor,
    private val url: String,
    private val trackPlayer: TrackPlayer,
    private val trackId: Int,
) : ViewModel() {

    private lateinit var binding: ActivityMediaBinding
    private val handler = Handler(Looper.getMainLooper())

    private var screenStateLiveData = MutableLiveData<Track>()
    private val playStatusLiveData = MutableLiveData<PlayStatus>()

    fun getScreenStateLiveData(): LiveData<Track> = screenStateLiveData
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    private fun preparePlayer() {
        if (url.isEmpty()) return
        mediaInteractor.preparePlayer(url) { state ->
            if (state == PlayerState.STATE_PREPARED) {
                handler.post {
                    with(handler) {
                        with(binding) {
                            imageViewPlay.isEnabled = true
                            textViewPlayTime.text = START_TIME
                            imageViewPlay.setImageResource(R.drawable.play_button)
                        }
                        removeCallbacks(createUpdateTimerTask())
                    }
                }
                playerState = PlayerState.STATE_PREPARED
            }
        }
    }

    private fun startPlayer() {
        mediaInteractor.startPlayer()
        binding.imageViewPlay.setImageResource(R.drawable.pause_button)
        playerState = PlayerState.STATE_PLAYING
        handler.post(createUpdateTimerTask())
    }

    private fun pausePlayer() {
        mediaInteractor.pausePlayer()
        binding.imageViewPlay.setImageResource(R.drawable.play_button)
        playerState = PlayerState.STATE_PAUSED
        handler.removeCallbacks(createUpdateTimerTask())
    }

    private fun playbackControl() {
        if (url.isEmpty()) {
            Toast.makeText(this, R.string.play_error, Toast.LENGTH_SHORT).show()
        } else {
            when (playerState) {
                PlayerState.STATE_PLAYING -> {
                    pausePlayer()
                }

                PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                    startPlayer()
                }

                PlayerState.STATE_DEFAULT -> {
                    preparePlayer()
                }
            }
        }
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerState.STATE_PLAYING) {
                    val currentPosition = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaInteractor.getCurrentPosition())
                    binding.textViewPlayTime.text = currentPosition
                    handler.postDelayed(this, PLAY_TIME_DELAY)
                }
            }

        }
    }

    fun play() {
        trackPlayer.play(
            trackId = trackId,
            // 1
            statusObserver = object : TrackPlayer.StatusObserver {
                override fun onProgress(progress: Float) {
                    // 2
                    playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
                }

                override fun onStop() {
                    // 3
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
                }

                override fun onPlay() {
                    // 4
                    playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
                }
            },
        )
    }

    // 5
    fun pause() {
        trackPlayer.pause(trackId)
    }

    // 6
    override fun onCleared() {
        trackPlayer.release(trackId)
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = 0f, isPlaying = false)
    }

    companion object {
        var playerState = PlayerState.STATE_DEFAULT
        private const val PLAY_TIME_DELAY = 500L
        private val APPLICATION_KEY = object : CreationExtras.Key<Any> {}

        fun getViewModelFactory(url: String, trackId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = (this[APPLICATION_KEY] as Creator).provideMediaInteractor()
                val trackPlayer = Creator.provideTrackPlayer()

                MediaViewModel(
                    interactor,
                    url,
                    trackPlayer,
                    trackId,
                )
            }
        }
    }
}