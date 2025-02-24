package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    private val viewStateFlow = MutableStateFlow<ViewState>(ViewState.Initial)
    fun getViewStateFlow() = viewStateFlow.asStateFlow()

    private var searchJob: Job? = null
    private var lastSearchQuery: String? = null

    fun triggerLastSearch() {
            searchInternal()
    }

    fun search(text: String?) {
        if (text.isNullOrBlank() || text == lastSearchQuery) return

        lastSearchQuery = text
        searchInternal()
    }

    private fun searchInternal() {
        searchJob = tracksInteractor.searchTracks(lastSearchQuery?.trim() ?: "")
            .onStart { viewStateFlow.emit(ViewState.Loading) }
            .onEach { viewStateFlow.emit(it) }
            .launchIn(viewModelScope)
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
        lastSearchQuery = null
        viewModelScope.launch {
            val history = tracksInteractor.getSearchHistory().toList()
            if (history.isEmpty()) {
                viewStateFlow.emit(ViewState.Initial)
            } else {
                viewStateFlow.emit(ViewState.History(history))
            }
        }
    }

    private companion object {
        private const val REPLAY_COUNT = 1
    }
}