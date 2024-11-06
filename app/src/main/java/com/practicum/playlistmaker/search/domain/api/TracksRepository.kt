package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<ViewState>
    fun getSearchHistory(): Array<Track>
    fun saveSearchTrackHistory(tracks: Array<Track>)
    fun addToTrackHistory(track: Track)
    fun clearHistory()
}