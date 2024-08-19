package com.practicum.playlistmaker.ui.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale

class MediaActivity : AppCompatActivity() {
    private var backButton: ImageView? = null
    private var imageViewAlbum: ImageView? = null
    private var imageViewCatalog: ImageView? = null
    private var imageViewPlay: ImageView? = null
    private var imageViewFavourite: ImageView? = null

    private var textViewTrackName: TextView? = null
    private var textViewArtistName: TextView? = null
    private var textViewPlayTime: TextView? = null
    private var textViewDuration: TextView? = null
    private var textViewDurationData: TextView? = null
    private var textViewAlbum: TextView? = null
    private var textViewAlbumData: TextView? = null
    private var textViewYear: TextView? = null
    private var textViewYearData: TextView? = null
    private var textViewGenre: TextView? = null
    private var textViewGenreData: TextView? = null
    private var textViewCountry: TextView? = null
    private var textViewCountryData: TextView? = null

    private var track: Track? = null
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var url: String? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        initViews()

        val jsonString = intent.getStringExtra(EXTRA_TRACK)
        track = jsonString?.let { Json.decodeFromString<Track>(it) }

        url = track?.trackUrl

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
        handler.removeCallbacks(createUpdateTimerTask())
    }

    private fun getImageAlbum() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        imageViewAlbum?.let {
            Glide.with(this).load(track?.coverArtworkMaxi).placeholder(R.drawable.place_holder)
                .fitCenter().transform(RoundedCorners(cornerRadius)).into(it)
        }
    }

    private fun setText() {
        textViewTrackName?.text = track?.trackName
        textViewArtistName?.text = track?.artistName
        textViewDurationData?.text = getTrackTime()
        textViewAlbumData?.text = getCollectionName()
        textViewYearData?.text = getReleaseDate()
        textViewGenreData?.text = getPrimaryGenreName()
        textViewCountryData?.text = getCountry()
        textViewPlayTime?.text = START_TIME
    }

    private fun getCollectionName(): String {
        return track?.collectionName ?: run {
            textViewAlbum?.isVisible = false
            textViewAlbumData?.isVisible = false
            ""
        }
    }

    private fun getPrimaryGenreName(): String {
        return track?.primaryGenreName ?: run {
            textViewGenre?.isVisible = false
            textViewGenreData?.isVisible = false
            ""
        }
    }

    private fun getCountry(): String {
        return track?.country ?: run {
            textViewCountry?.isVisible = false
            textViewCountryData?.isVisible = false
            ""
        }
    }

    private fun getReleaseDate(): String {
        return track?.releaseYear ?: run {
            textViewYear?.isVisible = false
            textViewYearData?.isVisible = false
            ""
        }
    }

    private fun getTrackTime(): String {
        return track?.trackTime ?: run {
            textViewDuration?.isVisible = false
            textViewDurationData?.isVisible = false
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
            handler.removeCallbacks(createUpdateTimerTask())
            textViewPlayTime?.text = START_TIME
            imageViewPlay?.setImageResource(R.drawable.play_button)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        imageViewPlay?.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        handler.post(createUpdateTimerTask())
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        imageViewPlay?.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
        handler.removeCallbacks(createUpdateTimerTask())
    }

    private fun playbackControl() {
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, R.string.play_error, Toast.LENGTH_SHORT).show()
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

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    val currentPosition = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    textViewPlayTime?.text = currentPosition
                    handler.postDelayed(this, PLAY_TIME_DELAY)
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
        textViewDuration = findViewById(R.id.textViewDuration)
        textViewDurationData = findViewById(R.id.textViewDurationData)
        textViewAlbum = findViewById(R.id.textViewAlbum)
        textViewAlbumData = findViewById(R.id.textViewAlbumData)
        textViewYear = findViewById(R.id.textViewYear)
        textViewYearData = findViewById(R.id.textViewYearData)
        textViewGenre = findViewById(R.id.textViewGenre)
        textViewGenreData = findViewById(R.id.textViewGenreData)
        textViewCountry = findViewById(R.id.textViewCountry)
        textViewCountryData = findViewById(R.id.textViewCountryData)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
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