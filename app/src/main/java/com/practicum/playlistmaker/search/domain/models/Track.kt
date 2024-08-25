package com.practicum.playlistmaker.search.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    @SerialName("trackName")
    val trackName: String,
    @SerialName("artistName")
    val artistName: String,
    @SerialName("trackTime")
    val trackTime: String?,
    @SerialName("coverArtworkMini")
    val coverArtworkMini: String?,
    @SerialName("coverArtworkMaxi")
    val coverArtworkMaxi: String?,
    @SerialName("trackId")
    val trackId: Int,
    @SerialName("collectionName")
    val collectionName: String?,
    @SerialName("releaseDate")
    val releaseYear: String?,
    @SerialName("primaryGenreName")
    val primaryGenreName: String?,
    @SerialName("country")
    val country: String?,
    @SerialName("trackUrl")
    val trackUrl: String?,
)