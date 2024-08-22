package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TracksRequest
import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.data.localStorage.SearchHistory
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: SearchHistory,
) : TracksRepository {
    override fun searchTracks(expression: String): ViewState {
        val response = networkClient.doRequest(TracksRequest(expression))
        when (response.resultCode) {
            200 -> return ViewState.Success((response as TracksResponse).results.map {
                Track(
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = it.getTrackTime(),
                    coverArtworkMini = it.artworkUrl100,
                    trackId = it.trackId,
                    collectionName = it.collectionName,
                    releaseYear = it.getReleaseYear(),
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    coverArtworkMaxi = it.getCoverArtwork(),
                    trackUrl = it.previewUrl,
                )
            })

            in 400..499 -> return ViewState.EmptyError
            else -> return ViewState.NetworkError
        }
    }

    override fun getSearchHistory(): Array<Track> {
        return localStorage.getSearchHistory()
    }

    override fun saveSearchTrackHistory(tracks: Array<Track>) {
        localStorage.saveSearchTrackHistory(tracks)
    }
}