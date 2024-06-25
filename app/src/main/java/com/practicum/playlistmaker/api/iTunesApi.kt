package com.practicum.playlistmaker.api

import com.practicum.playlistmaker.models.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    fun searchTracks(@Query("term") text: String) : Call<TracksResponse>
}