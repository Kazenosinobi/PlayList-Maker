package com.practicum.playlistmaker.playListScreen.domain.impl

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.playListScreen.domain.db.PlayListScreenInteractor
import com.practicum.playlistmaker.playListScreen.domain.db.PlayListScreenRepository
import kotlinx.coroutines.flow.Flow

class PlayListScreenInteractorImpl(
    private val playListScreenRepository: PlayListScreenRepository,
) : PlayListScreenInteractor {

    override suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData) {
        playListScreenRepository.deletePlayListAtFavouritePlayList(playList)
    }

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        playListScreenRepository.updateFavouritePlayList(playList)
    }

    override fun getPlayListById(playListId: Int): Flow<PlayListCreateData> =
        playListScreenRepository.getPlayListById(playListId)
}