package com.practicum.playlistmaker.mediaLibrary.domain.models

import com.practicum.playlistmaker.mediaLibrary.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayListData(
    @SerialName("playListId")
    val playListId: Long,
    @SerialName("image")
    val image: String?,
    @SerialName("nameOfAlbum")
    val nameOfAlbum: String?,
    @SerialName("descriptionOfAlbum")
    val descriptionOfAlbum: String?,
    @SerialName("tracksId")
    val tracksId: List<Int>,
    @SerialName("countTracks")
    val countTracks: Int,
) {
    fun addTrackId(trackId: Int): PlayListData {
        return this.copy(
            tracksId = buildList {
                this.add(trackId)
                this.addAll(this@PlayListData.tracksId)
            },
            countTracks = this.countTracks + 1
        )
    }

    fun removeTrack(track: Track): PlayListData {
        return this.copy(
            tracksId = this.tracksId.minus(track.trackId),
            countTracks = this.countTracks - 1
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

fun PlayListData.mapToPlayListEntity(): PlayListEntity {
    return PlayListEntity(
        playListId = playListId,
        playListName = nameOfAlbum,
        playListDescription = descriptionOfAlbum,
        imagePath = image,
        tracksId = tracksId,
        countTracks = countTracks
    )
}
