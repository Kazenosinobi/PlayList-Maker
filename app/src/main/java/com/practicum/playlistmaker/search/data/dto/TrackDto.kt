package com.practicum.playlistmaker.search.data.dto

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
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

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    fun getReleaseYear() = releaseDate?.take(GET_YEAR)

    companion object {
        const val GET_YEAR = 4

    }
}

fun TrackDto.mapToTrack(): Track {
    return Track(
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        coverArtworkMini = this.artworkUrl100,
        trackId = this.trackId,
        collectionName = this.collectionName,
        releaseYear = this.getReleaseYear(),
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        coverArtworkMaxi = this.getCoverArtwork(),
        trackUrl = this.previewUrl,
    )
}