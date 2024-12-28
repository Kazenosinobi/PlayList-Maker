package com.practicum.playlistmaker.mediaLibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData

@Entity(tableName = "play_list_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playListId: Long,
    val playListName: String?,
    val playListDescription: String?,
    val imagePath: String?,
    val tracksId: List<Int>,
    val countTracks: Int,
)

fun PlayListEntity.mapToPlayListCreateData(): PlayListData {
    return PlayListData(
        playListId = playListId,
        image = imagePath ?: "",
        nameOfAlbum = playListName ?: "",
        descriptionOfAlbum = playListDescription ?: "",
        tracksId = tracksId,
        countTracks = countTracks
    )
}
