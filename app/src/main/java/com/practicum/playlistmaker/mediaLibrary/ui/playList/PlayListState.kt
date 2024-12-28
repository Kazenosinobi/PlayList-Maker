package com.practicum.playlistmaker.mediaLibrary.ui.playList

import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData

sealed interface PlayListState {

    data class Content(val playLists: List<PlayListData>) : PlayListState
    data object Empty : PlayListState

}