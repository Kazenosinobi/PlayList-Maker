package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesService: ITunesApi,
    ) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TracksRequest) {
                try {
                    val resp = iTunesService.searchTracks(dto.expression)
                    resp.apply { resultCode = GOOD_REQUEST }
                } catch (e: Exception) {
                    Response().apply { resultCode = THROW_REQUEST }
                }

        } else {
            Response().apply { resultCode = BAD_REQUEST }
        }
    }

    private companion object {
        private const val BAD_REQUEST = 400
        private const val GOOD_REQUEST = 200
        private const val THROW_REQUEST = 500
    }
}