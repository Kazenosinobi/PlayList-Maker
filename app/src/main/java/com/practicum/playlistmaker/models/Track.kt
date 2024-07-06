package com.practicum.playlistmaker.models

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
    val artworkUrl100: String,
    @SerialName("trackId")
    val trackId: Int,
) {
    fun getTrackTime(): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}