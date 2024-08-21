package com.practicum.playlistmaker.media.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.Creator
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.media.domain.model.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding

    private var track: Track? = null

    private val interactor by lazy { Creator.provideMediaInteractor() }
    private var playerState = PlayerState.STATE_DEFAULT
    private var url: String? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val jsonString = intent.getStringExtra(EXTRA_TRACK)
        track = jsonString?.let { Json.decodeFromString<Track>(it) }

        url = track?.trackUrl

        binding.backButton.setOnClickListener {
            finish()
        }

        getImageAlbum()
        setText()
        preparePlayer()

        binding.imageViewPlay.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        interactor.getRelease()
        handler.removeCallbacks(createUpdateTimerTask())
        super.onDestroy()
    }

    private fun getImageAlbum() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        Glide.with(this)
            .load(track?.coverArtworkMaxi)
            .placeholder(R.drawable.place_holder)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.imageViewAlbum)
    }

    private fun setText() {
        with(binding) {
            textViewTrackName.text = track?.trackName
            textViewArtistName.text = track?.artistName
            textViewDurationData.text = getTrackTime()
            textViewAlbumData.text = getCollectionName()
            textViewYearData.text = getReleaseDate()
            textViewGenreData.text = getPrimaryGenreName()
            textViewCountryData.text = getCountry()
            textViewPlayTime.text = START_TIME
        }
    }

    private fun getCollectionName(): String {
        return track?.collectionName ?: run {
            with(binding) {
                textViewAlbum.isVisible = false
                textViewAlbumData.isVisible = false
                ""
            }
        }
    }

    private fun getPrimaryGenreName(): String {
        return track?.primaryGenreName ?: run {
            with(binding) {
                textViewGenre.isVisible = false
                textViewGenreData.isVisible = false
                ""
            }
        }
    }

    private fun getCountry(): String {
        return track?.country ?: run {
            with(binding) {
                textViewCountry.isVisible = false
                textViewCountryData.isVisible = false
                ""
            }
        }
    }

    private fun getReleaseDate(): String {
        return track?.releaseYear ?: run {
            with(binding) {
                textViewYear.isVisible = false
                textViewYearData.isVisible = false
                ""
            }
        }
    }

    private fun getTrackTime(): String {
        return track?.trackTime ?: run {
            with(binding) {
                textViewDuration.isVisible = false
                textViewDurationData.isVisible = false
                ""
            }
        }
    }

    private fun preparePlayer() {
        if (url.isNullOrEmpty()) return
        interactor.preparePlayer(url ?: "") { state ->
            if (state == PlayerState.STATE_PREPARED) {
                with(handler) {
                    with(binding) {
                        imageViewPlay.isEnabled = true
                        textViewPlayTime.text = START_TIME
                        imageViewPlay.setImageResource(R.drawable.play_button)
                    }
                    removeCallbacks(createUpdateTimerTask())
                }
                playerState = PlayerState.STATE_PREPARED
            }
        }
    }

    private fun startPlayer() {
        interactor.startPlayer()
        binding.imageViewPlay.setImageResource(R.drawable.pause_button)
        playerState = PlayerState.STATE_PLAYING
        handler.post(createUpdateTimerTask())
    }

    private fun pausePlayer() {
        interactor.pausePlayer()
        binding.imageViewPlay.setImageResource(R.drawable.play_button)
        playerState = PlayerState.STATE_PAUSED
        handler.removeCallbacks(createUpdateTimerTask())
    }

    private fun playbackControl() {
        if (url.isNullOrEmpty()) {
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
                    ).format(interactor.getCurrentPosition())
                    binding.textViewPlayTime.text = currentPosition
                    handler.postDelayed(this, PLAY_TIME_DELAY)
                }
            }

        }
    }

    companion object {
        private const val PLAY_TIME_DELAY = 500L
        private const val START_TIME = "00:00"
        private const val EXTRA_TRACK = "extra_track"

        fun createMediaActivityIntent(context: Context, track: Track): Intent {
            val intent = Intent(context, MediaActivity::class.java)
            val jsonString = Json.encodeToString(track)
            intent.putExtra(EXTRA_TRACK, jsonString)
            return intent
        }
    }
}