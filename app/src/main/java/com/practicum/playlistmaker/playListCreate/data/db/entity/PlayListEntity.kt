package com.practicum.playlistmaker.playListCreate.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData

@Entity(tableName = "play_list_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playListId: Int,
    val playListName: String?,
    val playListDescription: String?,
    val imagePath: String?,
    var trackIds: String,
    val trackCount: Int,
) {
    fun setTrackIds(tracks: List<Int>) {
        this.trackIds = Gson().toJson(tracks)
    }

    fun getTrackIdsFromJson(): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(trackIds, type)
    }
}

fun PlayListEntity.mapToPlayListEntity(): PlayListCreateData {
    return PlayListCreateData(
        image = imagePath,
        nameOfAlbum = playListName,
        descriptionOfAlbum = playListDescription,
    )
}
