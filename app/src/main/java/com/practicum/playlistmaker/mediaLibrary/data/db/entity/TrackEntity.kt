package com.practicum.playlistmaker.mediaLibrary.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practicum.playlistmaker.search.domain.models.Track

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val coverArtworkMini: String?,
    val coverArtworkMaxi: String?,
    val collectionName: String?,
    val releaseYear: String?,
    val primaryGenreName: String?,
    val country: String?,
    val trackUrl: String?,

    @ColumnInfo(name = "addedDate")
    val addedDate: Long,
)

fun TrackEntity.mapToTrack(): Track {
    return Track(
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTime,
        coverArtworkMini = this.coverArtworkMini,
        trackId = this.trackId,
        collectionName = this.collectionName,
        releaseYear = this.releaseYear,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        coverArtworkMaxi = this.coverArtworkMaxi,
        trackUrl = this.trackUrl,
    )
}
