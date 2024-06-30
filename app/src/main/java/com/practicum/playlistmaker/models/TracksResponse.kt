package com.practicum.playlistmaker.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TracksResponse(
    @SerialName("results")
    val results : List<Track>
)
