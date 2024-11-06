package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TrackDto

interface NetworkClient {
    suspend fun doRequest(expression: String): Result<List<TrackDto>>
}