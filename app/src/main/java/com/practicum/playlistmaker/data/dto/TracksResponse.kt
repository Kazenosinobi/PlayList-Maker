package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TracksResponse(
    @SerialName("results")
    val results : List<TrackDto>
) : Response()
