package com.practicum.playlistmaker.mediaLibrary.domain.impl

import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListRepository
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val playListRepository: PlayListRepository,
) : PlayListInteractor {

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        playListRepository.updateFavouritePlayList(playList)
    }

    override fun getFavouritePlayList(): Flow<List<PlayListCreateData>> =
        playListRepository.getFavouritePlayList()

}