package com.practicum.playlistmaker.playListScreen.ui

import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData

sealed interface PlayListScreenState {

    data class Content(val playList: PlayListData) : PlayListScreenState
    data object Empty : PlayListScreenState

}