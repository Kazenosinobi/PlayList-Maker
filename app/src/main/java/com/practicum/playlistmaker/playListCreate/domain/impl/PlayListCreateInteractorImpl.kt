package com.practicum.playlistmaker.playListCreate.domain.impl

import com.practicum.playlistmaker.playListCreate.domain.db.PlayListCreateInteractor
import com.practicum.playlistmaker.playListCreate.domain.db.PlayListCreateRepository
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

class PlayListCreateInteractorImpl(
    private val playListCreateRepository: PlayListCreateRepository,
) : PlayListCreateInteractor {

    override suspend fun addPlayListToFavouritePlayList(playList: PlayListCreateData) {
        playListCreateRepository.addPlayListToFavouritePlayList(playList)
    }

    override suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData) {
        playListCreateRepository.deletePlayListAtFavouritePlayList(playList)
    }

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        playListCreateRepository.updateFavouritePlayList(playList)
    }

    override fun getFavouritePlayList(): Flow<List<PlayListCreateData>> =
        playListCreateRepository.getFavouritePlayList()

}