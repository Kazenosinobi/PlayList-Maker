package com.practicum.playlistmaker.media.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.media.domain.model.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaActivity : AppCompatActivity() {
    private var binding: ActivityMediaBinding? = null

    private val track by lazy {
        val jsonString = intent.getStringExtra(EXTRA_TRACK) ?: ""
        Json.decodeFromString<Track>(jsonString)
    }

    private val viewModel by viewModel<MediaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        viewModel.getPlayStatusLiveData().observe(this) { state ->
            when (state) {
                PlayerState.STATE_DEFAULT -> {
                    val url = track.trackUrl
                    if (url.isNullOrBlank()) {
                        Toast.makeText(this, R.string.play_error, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.preparePlayer(url)
                    }
                }

                PlayerState.STATE_PREPARED -> {
                    binding?.let {
                        it.imageViewPlay.isEnabled = true
                        it.textViewPlayTime.text = START_TIME
                        it.imageViewPlay.setImageResource(R.drawable.play_button)
                    }
                }

                PlayerState.STATE_PLAYING -> binding?.imageViewPlay?.setImageResource(
                    R.drawable.pause_button
                )

                PlayerState.STATE_PAUSED -> binding?.imageViewPlay?.setImageResource(
                    R.drawable.play_button
                )

                null -> Unit
            }
        }

        viewModel.getCurrentPositionLiveData().observe(this) {
            binding?.textViewPlayTime?.text = it
        }

        binding?.backButton?.setOnClickListener {
            finish()
        }

        getImageAlbum()
        setText()

        binding?.imageViewPlay?.setOnClickListener {
            viewModel.playbackControl(track.trackUrl ?: "")
        }
    }

    override fun onPause() {
        viewModel.pausePlayer()
        super.onPause()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun getImageAlbum() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        binding?.let {
            Glide.with(this)
                .load(track.coverArtworkMaxi)
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(cornerRadius))
                .into(it.imageViewAlbum)
        }
    }

    private fun setText() {
        binding.let {
            it?.textViewTrackName?.text = track.trackName
            it?.textViewArtistName?.text = track.artistName
            it?.textViewDurationData?.text = getTrackTime()
            it?.textViewAlbumData?.text = getCollectionName()
            it?.textViewYearData?.text = getReleaseDate()
            it?.textViewGenreData?.text = getPrimaryGenreName()
            it?.textViewCountryData?.text = getCountry()
            it?.textViewPlayTime?.text = START_TIME
        }
    }

    private fun getCollectionName(): String {
        return track.collectionName ?: run {
            binding.let {
                it?.textViewAlbum?.isVisible = false
                it?.textViewAlbumData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun getPrimaryGenreName(): String {
        return track.primaryGenreName ?: run {
            binding.let {
                it?.textViewGenre?.isVisible = false
                it?.textViewGenreData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun getCountry(): String {
        return track.country ?: run {
            binding.let {
                it?.textViewCountry?.isVisible = false
                it?.textViewCountryData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun getReleaseDate(): String {
        return track.releaseYear ?: run {
            binding.let {
                it?.textViewYear?.isVisible = false
                it?.textViewYearData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    private fun getTrackTime(): String {
        return track.trackTime ?: run {
            binding.let {
                it?.textViewDuration?.isVisible = false
                it?.textViewDurationData?.isVisible = false
                EMPTY_STRING
            }
        }
    }

    companion object {
        const val START_TIME = "00:00"
        private const val EXTRA_TRACK = "extra_track"
        private const val EMPTY_STRING = ""

        fun createMediaActivityIntent(context: Context, track: Track): Intent {
            val intent = Intent(context, MediaActivity::class.java)
            val jsonString = Json.encodeToString(track)
            intent.putExtra(EXTRA_TRACK, jsonString)
            return intent
        }
    }
}