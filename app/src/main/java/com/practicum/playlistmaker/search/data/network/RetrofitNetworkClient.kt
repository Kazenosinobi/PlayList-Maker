package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksRequest

class RetrofitNetworkClient(
    private val iTunesService: iTunesApi,
    ) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TracksRequest) {
            val resp = iTunesService.searchTracks(dto.expression).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = BAD_REQUEST }
        }
    }

    private companion object {
        private const val BAD_REQUEST = 400
    }
}