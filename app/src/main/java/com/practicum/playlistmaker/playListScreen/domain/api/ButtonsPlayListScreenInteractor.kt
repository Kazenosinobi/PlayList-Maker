package com.practicum.playlistmaker.playListScreen.domain.api

import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData

interface ButtonsPlayListScreenInteractor {
    fun share(playList: PlayListCreateData)
}