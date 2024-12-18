package com.practicum.playlistmaker.playListCreate.domain.models

import com.practicum.playlistmaker.playListCreate.data.db.entity.PlayListEntity

data class PlayListCreateData(
    val image: String?,
    val nameOfAlbum: String?,
    val descriptionOfAlbum: String?,
)

fun PlayListCreateData.mapToPlayListEntity(): PlayListEntity {
    return PlayListEntity(
        playListId = 0,
        playListName = nameOfAlbum,
        playListDescription = descriptionOfAlbum,
        imagePath = image,
        trackIds = "[]",
        trackCount = 0,
    )
}
