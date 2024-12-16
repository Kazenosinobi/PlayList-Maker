package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TrackDto

class RetrofitNetworkClient(
    private val iTunesService: ITunesApi,
) : NetworkClient {

    override suspend fun doRequest(expression: String): Result<List<TrackDto>> {
        return runCatching {
            iTunesService.searchTracks(expression).results
        }
    }
}