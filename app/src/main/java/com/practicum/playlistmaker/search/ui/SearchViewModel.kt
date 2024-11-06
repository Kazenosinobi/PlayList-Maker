package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    private val viewStateSharedFlow = MutableSharedFlow<ViewState>()
    fun getCurrentPositionSharedFlow() = viewStateSharedFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun search(text: String?) {
        if (text.isNullOrBlank()) return

        searchJob = viewModelScope.launch {
            viewStateSharedFlow.emit(ViewState.Loading)
            tracksInteractor
                .searchTracks(text.trim())
                .collect { viewState ->
                    viewStateSharedFlow.emit(viewState)
                }
        }
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        needToShowHistory()
    }

    fun addToTrackHistory(track: Track) {
        tracksInteractor.addToTrackHistory(track)
    }

    fun needToShowHistory() {
        searchJob?.cancel()
        viewModelScope.launch {
            viewStateSharedFlow.emit(
                ViewState.History(
                    tracksInteractor.getSearchHistory().toList()
                )
            )
        }
    }
}