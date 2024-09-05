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

        tracksInteractor.searchTracks(text.trim()) { viewState ->
            viewStateLiveData.postValue(viewState)
        }
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        needToShowHistory()
    }

    fun addToTrackHistory(track: Track) {
        tracksInteractor.addToTrackHistory(track)
        needToShowHistory()
    }

    fun needToShowHistory() {
        viewStateLiveData.value = ViewState.History(tracksInteractor.getSearchHistory().toList())
    }

    companion object {

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