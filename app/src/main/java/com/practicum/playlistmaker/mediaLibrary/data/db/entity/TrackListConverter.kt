package com.practicum.playlistmaker.mediaLibrary.data.db.entity

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TrackListConverter {

    private val json = Json

    @TypeConverter
    fun fromTrackIdList(tracksId: List<Int>): String {
        return json.encodeToString(tracksId)
    }

    @TypeConverter
    fun toTrackIdList(tracksJson: String): List<Int> {
        return json.decodeFromString(tracksJson)
    }
}