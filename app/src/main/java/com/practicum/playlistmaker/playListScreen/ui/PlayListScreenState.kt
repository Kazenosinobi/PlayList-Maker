package com.practicum.playlistmaker.playListScreen.ui

import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.search.domain.models.Track

sealed interface PlayListScreenState {

    data class Content(val playList: PlayListData, val tracks: List<Track> = emptyList()) : PlayListScreenState
    data object Empty : PlayListScreenState

}