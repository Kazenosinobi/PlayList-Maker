package com.practicum.playlistmaker.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.activity.SearchActivity.Companion.TRACK_KEY
import com.practicum.playlistmaker.models.Track
import kotlinx.serialization.json.Json

class MediaActivity : AppCompatActivity() {
    private var backButton: ImageView? = null
    private var imageViewAlbum: ImageView? = null
    private var imageViewCatalog: ImageView? = null
    private var imageViewPlay: ImageView? = null
    private var imageViewFavourite: ImageView? = null

    private var textViewTrackName: TextView? = null
    private var textViewArtistName: TextView? = null
    private var textViewPlayTime: TextView? = null
    private var textViewDurationData: TextView? = null
    private var textViewAlbum: TextView? = null
    private var textViewAlbumData: TextView? = null
    private var textViewYearData: TextView? = null
    private var textViewGenreData: TextView? = null
    private var textViewCountryData: TextView? = null

    private var track: Track? = null
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        initViews()

        val jsonString = intent.getStringExtra(TRACK_KEY)
        track = jsonString?.let { Json.decodeFromString<Track>(it) }

//        url = track?.previewUrl
        Log.d("MediaActivity", "$url")

        backButton?.setOnClickListener {
            finish()
        }

        getImageAlbum()
        setText()
        preparePlayer()

        imageViewPlay?.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun getImageAlbum() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        val coverUrl = track?.getCoverArtwork()
        Log.d("MediaActivity", "Cover URL: $coverUrl")
        imageViewAlbum?.let {
            Glide.with(this).load(track?.getCoverArtwork()).placeholder(R.drawable.place_holder)
                .fitCenter().transform(RoundedCorners(cornerRadius)).into(it)
        }
    }

    private fun setText() {
        textViewTrackName?.text = track?.trackName
        textViewArtistName?.text = track?.artistName
        textViewDurationData?.text = track?.getTrackTime()
        textViewAlbumData?.text = getCollectionName()
        textViewYearData?.text = track?.getReleaseYear()
        textViewGenreData?.text = track?.primaryGenreName
        textViewCountryData?.text = track?.country
    }

    private fun getCollectionName(): String {
        return track?.collectionName ?: run {
            textViewAlbum?.isVisible = false
            textViewAlbumData?.isVisible = false
            ""
        }
    }

    private fun preparePlayer() {
        if (url.isNullOrEmpty()) return
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                imageViewPlay?.isEnabled = true
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
            }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        imageViewPlay?.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        imageViewPlay?.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show()
        } else {
            when (playerState) {
                STATE_PLAYING -> {
                    pausePlayer()
                }

                STATE_PREPARED, STATE_PAUSED -> {
                    startPlayer()
                }
            }
        }
    }

    private fun initViews() {
        backButton = findViewById(R.id.backButton)
        imageViewAlbum = findViewById(R.id.imageViewAlbum)
        textViewTrackName = findViewById(R.id.textViewTrackName)
        textViewArtistName = findViewById(R.id.textViewArtistName)
        imageViewCatalog = findViewById(R.id.imageViewCatalog)
        imageViewPlay = findViewById(R.id.imageViewPlay)
        imageViewFavourite = findViewById(R.id.imageViewFavourite)
        textViewPlayTime = findViewById(R.id.textViewPlayTime)
        textViewDurationData = findViewById(R.id.textViewDurationData)
        textViewAlbum = findViewById(R.id.textViewAlbum)
        textViewAlbumData = findViewById(R.id.textViewAlbumData)
        textViewYearData = findViewById(R.id.textViewYearData)
        textViewGenreData = findViewById(R.id.textViewGenreData)
        textViewCountryData = findViewById(R.id.textViewCountryData)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}