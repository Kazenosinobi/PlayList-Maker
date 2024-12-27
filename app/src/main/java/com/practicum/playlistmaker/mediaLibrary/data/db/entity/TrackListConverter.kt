package com.practicum.playlistmaker.mediaLibrary.data.db.entity

import androidx.room.TypeConverter
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TrackListConverter {

    private val json = Json

    @TypeConverter
    fun fromTrackList(tracks: List<Track>): String {
        return json.encodeToString(tracks)
    }

    @TypeConverter
    fun toTrackList(tracksJson: String): List<Track> {
        return json.decodeFromString(tracksJson)
    }
}