package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TracksRequest
import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.data.localStorage.SearchHistory
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: SearchHistory,
) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksRequest(expression))
        if (response.resultCode == SUCCESS_REQUEST) {
            return (response as TracksResponse).results.map {
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
            }
        } else {
            return emptyList()
        }
    }

    override fun getSearchHistory(): Array<Track> {
        return localStorage.getSearchHistory()
    }

    override fun saveSearchTrackHistory(tracks: Array<Track>) {
        localStorage.saveSearchTrackHistory(tracks)
    }

    private companion object {
        private const val SUCCESS_REQUEST = 200
    }
}