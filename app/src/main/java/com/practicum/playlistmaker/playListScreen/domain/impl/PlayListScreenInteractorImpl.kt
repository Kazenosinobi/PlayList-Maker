package com.practicum.playlistmaker.playListScreen.domain.impl

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.playListScreen.domain.db.PlayListScreenInteractor
import com.practicum.playlistmaker.playListScreen.domain.db.PlayListScreenRepository

class PlayListScreenInteractorImpl(
    private val playListScreenRepository: PlayListScreenRepository,
) : PlayListScreenInteractor {

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        playListScreenRepository.updateFavouritePlayList(playList)
    }

    override suspend fun getPlayListById(playListId: Int): PlayListCreateData =
        playListScreenRepository.getPlayListById(playListId)
}