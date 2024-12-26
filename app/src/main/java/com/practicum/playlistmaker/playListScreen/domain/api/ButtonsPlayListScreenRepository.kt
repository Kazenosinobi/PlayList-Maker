package com.practicum.playlistmaker.playListScreen.domain.api

import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData

interface ButtonsPlayListScreenRepository {
    fun share(playList: PlayListCreateData)
}