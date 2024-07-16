package com.practicum.playlistmaker.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
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
    private var textViewTrackName: TextView? = null
    private var textViewArtistName: TextView? = null
    private var imageViewCatalog: ImageView? = null
    private var imageViewPlay: ImageView? = null
    private var imageViewFavourite: ImageView? = null
    private var textViewPlayTime: TextView? = null
    private var textViewDurationData: TextView? = null
    private var textViewAlbum: TextView? = null
    private var textViewAlbumData: TextView? = null
    private var textViewYearData: TextView? = null
    private var textViewGenreData: TextView? = null
    private var textViewCountryData: TextView? = null

    private var track: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        initViews()

        val jsonString = intent.getStringExtra(TRACK_KEY)
        track = jsonString?.let { Json.decodeFromString<Track>(it) }

        backButton?.setOnClickListener {
            finish()
        }

        getImageAlbum()
        setText()
    }

    private fun getImageAlbum() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen._8dp)
        val coverUrl = track?.getCoverArtwork()
        Log.d("MediaActivity", "Cover URL: $coverUrl")
        imageViewAlbum?.let {
            Glide.with(this)
                .load(track?.getCoverArtwork())
                .placeholder(R.drawable.place_holder)
                .fitCenter()
                .transform(RoundedCorners(cornerRadius))
                .into(it)
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
}