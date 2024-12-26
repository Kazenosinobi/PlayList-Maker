package com.practicum.playlistmaker.playListScreen.ui

import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData

sealed interface PlayListScreenState {

    data class Content(val playList: PlayListCreateData) : PlayListScreenState
    data object Empty : PlayListScreenState

}