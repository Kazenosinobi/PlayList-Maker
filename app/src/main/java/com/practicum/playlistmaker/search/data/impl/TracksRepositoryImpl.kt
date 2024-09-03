package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.dto.TracksRequest
import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.data.dto.mapToTrack
import com.practicum.playlistmaker.search.data.localStorage.SearchHistory
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import java.lang.Exception

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: SearchHistory,
) : TracksRepository {
    override fun searchTracks(expression: String): ViewState {
        try {
            val response = networkClient.doRequest(TracksRequest(expression))

            when (response.resultCode) {
                in 200..299 -> {
                    val result = (response as TracksResponse).results
                    return if (result.isEmpty()) {
                        ViewState.EmptyError
                    } else {
                        ViewState.Success(result.map {
                            it.mapToTrack()
                        })
                    }
                }
                else -> return ViewState.EmptyError
            }
        } catch (e: Exception) {
            return ViewState.NetworkError
        }
    }

    override fun getSearchHistory(): Array<Track> {
        return localStorage.getSearchHistory()
    }

    override fun saveSearchTrackHistory(tracks: Array<Track>) {
        localStorage.saveSearchTrackHistory(tracks)
    }
}