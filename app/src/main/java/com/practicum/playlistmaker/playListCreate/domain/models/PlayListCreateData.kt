package com.practicum.playlistmaker.playListCreate.domain.models

import com.practicum.playlistmaker.playListCreate.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayListCreateData(
    @SerialName("playListId")
    val playListId: Long,
    @SerialName("image")
    val image: String?,
    @SerialName("nameOfAlbum")
    val nameOfAlbum: String?,
    @SerialName("descriptionOfAlbum")
    val descriptionOfAlbum: String?,
    @SerialName("tracks")
    val tracks: List<Track>
) {
    fun addTrack(track: Track): PlayListCreateData {
        return this.copy(
            tracks = this.tracks.plus(track),
        )
    }

    fun removeTrack(track: Track): PlayListCreateData {
        return this.copy(
            tracks = this.tracks.minus(track),
        )
    }

    fun getTotalDuration(tracks: List<Track>): Long {
        val totalDuration = tracks.sumOf { it.trackTimeMillis }
        return totalDuration / MILLIS_IN_SECOND
    }

    companion object {
        private const val MILLIS_IN_SECOND = 60_000L
    }
}

fun PlayListCreateData.mapToPlayListEntity(): PlayListEntity {
    return PlayListEntity(
        playListId = playListId,
        playListName = nameOfAlbum,
        playListDescription = descriptionOfAlbum,
        imagePath = image,
        tracks = tracks,
    )
}
