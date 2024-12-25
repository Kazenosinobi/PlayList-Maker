package com.practicum.playlistmaker.playListScreen.domain.api

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData

interface ButtonsPlayListScreenRepository {
    fun share(playList: PlayListCreateData)
}