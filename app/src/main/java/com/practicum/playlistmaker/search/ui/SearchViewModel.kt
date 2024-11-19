package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    private val viewStateSharedFlow = MutableSharedFlow<ViewState>(replay = REPLAY_COUNT)
    fun getCurrentPositionSharedFlow() = viewStateSharedFlow.asSharedFlow()

    private var searchJob: Job? = null
    private var lastSearchQuery: String? = null

    fun search(text: String?) {
        if (text.isNullOrBlank() || text == lastSearchQuery) return

        lastSearchQuery = text
        searchJob = tracksInteractor.searchTracks(text.trim())
            .onStart { viewStateSharedFlow.emit(ViewState.Loading) }
            .onEach { viewStateSharedFlow.emit(it) }
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
        viewModelScope.launch {
            viewStateSharedFlow.emit(
                ViewState.History(
                    tracksInteractor.getSearchHistory().toList()
                )
            )
        }
    }

    private companion object {
        private const val REPLAY_COUNT = 1
    }
}