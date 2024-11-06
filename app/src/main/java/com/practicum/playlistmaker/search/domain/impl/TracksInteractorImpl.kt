package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(
    private val repository: TracksRepository,
) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<ViewState> {
            return repository.searchTracks(expression)
    }

    override fun getSearchHistory(): Array<Track> {
        return repository.getSearchHistory()
    }

    override fun saveSearchTrackHistory(tracks: Array<Track>) {
        repository.saveSearchTrackHistory(tracks)
    }

    override fun addToTrackHistory(track: Track) {
        repository.addToTrackHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}