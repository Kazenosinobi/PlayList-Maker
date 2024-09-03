package com.practicum.playlistmaker.search.data.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState

interface TracksRepository {
    fun searchTracks(expression: String): ViewState
    fun getSearchHistory(): Array<Track>
    fun saveSearchTrackHistory(tracks: Array<Track>)
}