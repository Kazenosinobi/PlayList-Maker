package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TracksRequest
import com.practicum.playlistmaker.data.dto.TracksResponse
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksRequest(expression))
        if (response.resultCode == SUCCESS_REQUEST) {
            return (response as TracksResponse).results.map {
                Track(
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.getReleaseYear(),
                    it.primaryGenreName,
                    it.country,
                    it.getCoverArtwork()
                )
            }
        } else {
            return emptyList()
        }
    }

    private companion object {
        private const val SUCCESS_REQUEST = 200
    }
}