package com.practicum.playlistmaker.mediaLibrary.ui.favourite

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavouriteState {

    data class Content(val tracks: List<Track>) : FavouriteState
    data object Empty : FavouriteState

}