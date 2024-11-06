package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.dto.TracksRequest
import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.data.dto.mapToTrack
import com.practicum.playlistmaker.search.data.localStorage.SearchHistory
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: SearchHistory,
) : TracksRepository {

    private val historyList = arrayListOf<Track>()

    init {
        loadHistory()
    }

    override fun searchTracks(expression: String): Flow<ViewState> = flow {
        try {
            val response = networkClient.doRequest(TracksRequest(expression))

            when (response.resultCode) {
                in 200..299 -> {
                    val result = (response as TracksResponse).results
                    if (result.isEmpty()) {
                        emit(ViewState.EmptyError)
                    } else {
                        emit(ViewState.Success(result.map {
                            it.mapToTrack()
                        }))
                    }
                }

                else -> emit(ViewState.EmptyError)
            }
        } catch (e: Exception) {
            emit(ViewState.NetworkError)
        }
    }

    override fun getSearchHistory(): Array<Track> {
        return localStorage.getSearchHistory()
    }

    override fun saveSearchTrackHistory(tracks: Array<Track>) {
        localStorage.saveSearchTrackHistory(tracks)
    }

    override fun addToTrackHistory(track: Track) {
        val existingTrackIndex = historyList.indexOfFirst { it.trackId == track.trackId }
        if (existingTrackIndex != -1) {
            historyList.removeAt(existingTrackIndex)
        }
        if (historyList.size >= TRACKS_HISTORY_MAX_SIZE) {
            historyList.removeAt(historyList.lastIndex)
        }
        historyList.add(0, track)

        saveSearchTrackHistory(historyList.toTypedArray())
    }

    override fun clearHistory() {
        historyList.clear()
        saveSearchTrackHistory(emptyArray())
    }

    private fun loadHistory() {
        historyList.addAll(getSearchHistory())
    }

    private companion object {
        private const val TRACKS_HISTORY_MAX_SIZE = 10
    }
}