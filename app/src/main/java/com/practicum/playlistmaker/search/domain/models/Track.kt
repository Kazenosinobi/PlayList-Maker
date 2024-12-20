package com.practicum.playlistmaker.search.domain.models

import com.practicum.playlistmaker.mediaLibrary.data.db.entity.TrackEntity
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
    @SerialName("isFavorite")
    val isFavorite: Boolean = false,
)

fun Track.mapToTrackEntity(): TrackEntity {
    return TrackEntity(
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        coverArtworkMini = this.coverArtworkMini,
        trackId = this.trackId,
        collectionName = this.collectionName,
        releaseYear = this.releaseYear,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        coverArtworkMaxi = this.coverArtworkMaxi,
        trackUrl = this.trackUrl,
        addedDate = System.currentTimeMillis(),
    )
}