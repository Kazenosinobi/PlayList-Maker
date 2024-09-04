package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.core.App
import com.practicum.playlistmaker.core.Creator
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    private val viewStateLiveData = MutableLiveData<ViewState>()
    fun getCurrentPositionLiveData(): LiveData<ViewState> = viewStateLiveData

    fun search(text: String?) {
        if (text.isNullOrBlank()) return
        viewStateLiveData.value = ViewState.Loading

        tracksInteractor.searchTracks(
            text.trim()
        ) {
            viewState -> viewStateLiveData.value = viewState
        }
    }

    fun clearHistory() {
//        trackHistoryAdapter?.submitList(emptyList())
        tracksInteractor.saveSearchTrackHistory(emptyArray())
//        binding.groupHistory.isVisible = false
    }

    fun addToTrackHistory(track: Track) {
        val currentTracks = trackHistoryAdapter?.currentList?.toMutableList() ?: mutableListOf()
        val existingTrackIndex = currentTracks.indexOfFirst { it.trackId == track.trackId }
        if (existingTrackIndex != -1) {
            currentTracks.removeAt(existingTrackIndex)
        }
        if (currentTracks.size >= TRACKS_HISTORY_MAX_SIZE) {
            currentTracks.removeAt(currentTracks.lastIndex)
        }
        currentTracks.add(0, track)
        trackHistoryAdapter?.submitList(currentTracks)
        tracksInteractor.saveSearchTrackHistory(
            currentTracks.toTypedArray()
        )
    }

    companion object {

        private const val TRACKS_HISTORY_MAX_SIZE = 10

        fun getViewModelFactory(application: App): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = Creator.provideTracksInteractor(application)

                SearchViewModel(
                    interactor,
                )
            }
        }
    }
}