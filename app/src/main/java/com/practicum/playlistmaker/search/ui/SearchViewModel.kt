package com.practicum.playlistmaker.search.ui

import android.util.Log
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

    private val historyList = arrayListOf<Track>()

    init {
        getHistory()
    }

    override fun onCleared() {

        super.onCleared()
    }

    fun search(text: String?) {
        if (text.isNullOrBlank()) return
        viewStateLiveData.value = ViewState.Loading

        tracksInteractor.searchTracks(
            text.trim()
        ) { viewState ->
            viewStateLiveData.postValue(viewState)
        }
    }

    fun clearHistory() {
        historyList.clear()
        tracksInteractor.saveSearchTrackHistory(emptyArray())
        viewStateLiveData.value = ViewState.History(historyList)
        Log.d("MyLog", "clearHistory: $historyList")
    }

    private fun getHistory() {
        historyList.addAll(tracksInteractor.getSearchHistory())
        Log.d("MyLog", "getHistory: $historyList")
    }

    fun addToTrackHistory(track: Track) {
        val existingTrackIndex = historyList.indexOfFirst { it.trackId == track.trackId }
        if (existingTrackIndex != -1) {
            historyList.removeAt(existingTrackIndex)
        }
        if (historyList.size >= TRACKS_HISTORY_MAX_SIZE) {
            historyList.removeAt(historyList.lastIndex)
        }
        historyList.add(0, track)
        tracksInteractor.saveSearchTrackHistory(
            historyList.toTypedArray()
        )
        tracksInteractor.saveSearchTrackHistory(
            historyList.toTypedArray()
        )
        Log.d("MyLog", "addToTrackHistory: $historyList")
    }

    fun needToShowHistory() {
        viewStateLiveData.value = ViewState.History(historyList)
        Log.d("MyLog", "needToShowHistory: $historyList")
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