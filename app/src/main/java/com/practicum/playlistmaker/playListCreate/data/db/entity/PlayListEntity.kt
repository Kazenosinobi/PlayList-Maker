package com.practicum.playlistmaker.playListCreate.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
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

fun PlayListEntity.mapToPlayListEntity(): PlayListCreateData {
    return PlayListCreateData(
        playListId = playListId,
        image = imagePath,
        nameOfAlbum = playListName,
        descriptionOfAlbum = playListDescription,
        tracks = tracks
    )
}
