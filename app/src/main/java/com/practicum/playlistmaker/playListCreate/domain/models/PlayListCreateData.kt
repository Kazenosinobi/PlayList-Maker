package com.practicum.playlistmaker.playListCreate.domain.models

import com.practicum.playlistmaker.playListCreate.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.search.domain.models.Track

data class PlayListCreateData(
    val playListId: Long,
    val image: String?,
    val nameOfAlbum: String?,
    val descriptionOfAlbum: String?,
    val tracks: List<Track>
) {
    fun addTrack(track: Track): PlayListCreateData {
        return this.copy(
            tracks = this.tracks + track,
        )
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
