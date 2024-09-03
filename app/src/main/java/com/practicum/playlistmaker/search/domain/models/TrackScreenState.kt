package com.practicum.playlistmaker.search.domain.models

sealed class TrackScreenState {
    data object Loading: TrackScreenState()
    data class Content(
        val track: Track,
    ): TrackScreenState()
}