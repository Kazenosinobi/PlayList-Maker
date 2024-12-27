package com.practicum.playlistmaker.mediaLibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.search.domain.models.Track

@Entity(tableName = "play_list_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playListId: Long,
    val playListName: String?,
    val playListDescription: String?,
    val imagePath: String?,
    val tracks: List<Track>,
)

fun PlayListEntity.mapToPlayListCreateData(): PlayListData {
    return PlayListData(
        playListId = playListId,
        image = imagePath ?: "",
        nameOfAlbum = playListName ?: "",
        descriptionOfAlbum = playListDescription ?: "",
        tracks = tracks
    )
}
