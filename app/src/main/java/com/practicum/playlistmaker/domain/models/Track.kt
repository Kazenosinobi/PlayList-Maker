package com.practicum.playlistmaker.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Observer

@Serializable
data class Track(
    @SerialName("trackName")
    val trackName: String,
    @SerialName("artistName")
    val artistName: String,
    @SerialName("trackTimeMillis")
    val trackTimeMillis: Long,
    @SerialName("artworkUrl100")
    val artworkUrl100: String?,
    @SerialName("trackId")
    val trackId: Int,
    @SerialName("collectionName")
    val collectionName: String?,
    @SerialName("releaseDate")
    val releaseDate: String?,
    @SerialName("primaryGenreName")
    val primaryGenreName: String?,
    @SerialName("country")
    val country: String?,
    @SerialName("previewUrl")
    val previewUrl: String?,
) {
    fun getTrackTime(): String? =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")

    fun getReleaseYear() = releaseDate?.take(TAKE_YEAR)

    companion object {
        const val TAKE_YEAR = 4
    }
}