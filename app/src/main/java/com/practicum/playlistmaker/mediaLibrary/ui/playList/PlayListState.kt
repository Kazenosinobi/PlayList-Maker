package com.practicum.playlistmaker.mediaLibrary.ui.playList

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData

sealed interface PlayListState {

    data class Content(val tracks: List<PlayListCreateData>) : PlayListState
    data object Empty : PlayListState

}