package com.practicum.playlistmaker.media.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.media.MediaViewModel
import com.practicum.playlistmaker.media.MediaViewModel.Companion.playerState
import com.practicum.playlistmaker.media.domain.model.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding

    private lateinit var track: Track

    //    private val interactor by lazy { Creator.provideMediaInteractor() }
    private lateinit var url: String

    private lateinit var viewModel: MediaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        url = track.trackUrl.toString()

        viewModel = ViewModelProvider(
            this,
            MediaViewModel.getViewModelFactory(url, track.trackId)
        )[MediaViewModel::class.java]

        viewModel.getScreenStateLiveData().observe(this) {
            getImageAlbum()
            setText()
        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changeButtonStyle(playStatus)
            binding.seekBar.value = playStatus.progress
        }

        val jsonString = intent.getStringExtra(EXTRA_TRACK) ?: ""
        track = Json.decodeFromString<Track>(jsonString)



        binding.backButton.setOnClickListener {
            finish()
        }



        preparePlayer()

        binding.imageViewPlay.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        if (playerState == PlayerState.STATE_PLAYING) {
            pausePlayer()
        }
        super.onPause()
    }

//    override fun onDestroy() {
//        interactor.getRelease()
//        super.onDestroy()
//    }

    private fun changeButtonStyle(playStatus: PlayStatus) {
        // Меняем вид кнопки проигрывания в зависимости от того, играет сейчас трек или нет
    }

    private fun getImageAlbum() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        Glide.with(this)
            .load(track.coverArtworkMaxi)
            .placeholder(R.drawable.place_holder)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.imageViewAlbum)
    }

    private fun setText() {
        with(binding) {
            textViewTrackName.text = track.trackName
            textViewArtistName.text = track.artistName
            textViewDurationData.text = getTrackTime()
            textViewAlbumData.text = getCollectionName()
            textViewYearData.text = getReleaseDate()
            textViewGenreData.text = getPrimaryGenreName()
            textViewCountryData.text = getCountry()
            textViewPlayTime.text = START_TIME
        }
    }

    private fun getCollectionName(): String {
        return track.collectionName ?: run {
            with(binding) {
                textViewAlbum.isVisible = false
                textViewAlbumData.isVisible = false
                ""
            }
        }
    }

    private fun getPrimaryGenreName(): String {
        return track.primaryGenreName ?: run {
            with(binding) {
                textViewGenre.isVisible = false
                textViewGenreData.isVisible = false
                ""
            }
        }
    }

    private fun getCountry(): String {
        return track.country ?: run {
            with(binding) {
                textViewCountry.isVisible = false
                textViewCountryData.isVisible = false
                ""
            }
        }
    }

    private fun getReleaseDate(): String {
        return track.releaseYear ?: run {
            with(binding) {
                textViewYear.isVisible = false
                textViewYearData.isVisible = false
                ""
            }
        }
    }

    private fun getTrackTime(): String {
        return track.trackTime ?: run {
            with(binding) {
                textViewDuration.isVisible = false
                textViewDurationData.isVisible = false
                ""
            }
        }
    }

    companion object {
        const val START_TIME = "00:00"
        private const val EXTRA_TRACK = "extra_track"

        fun createMediaActivityIntent(context: Context, track: Track): Intent {
            val intent = Intent(context, MediaActivity::class.java)
            val jsonString = Json.encodeToString(track)
            intent.putExtra(EXTRA_TRACK, jsonString)
            return intent
        }
    }
}