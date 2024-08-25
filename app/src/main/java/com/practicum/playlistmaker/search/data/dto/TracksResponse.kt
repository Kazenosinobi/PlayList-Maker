package com.practicum.playlistmaker.search.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TracksResponse(
    @SerialName("results")
    val results : List<TrackDto>
) : Response()
