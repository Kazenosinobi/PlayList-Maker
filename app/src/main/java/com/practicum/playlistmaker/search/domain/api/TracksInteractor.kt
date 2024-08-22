package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    fun getSearchHistory(): Array<Track>
    fun saveSearchTrackHistory(tracks: Array<Track>)

    fun interface TracksConsumer {
        fun consume(viewState: ViewState)
    }
}