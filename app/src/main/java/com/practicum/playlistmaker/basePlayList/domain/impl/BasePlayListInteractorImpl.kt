package com.practicum.playlistmaker.basePlayList.domain.impl

import android.util.Log
import com.practicum.playlistmaker.basePlayList.domain.db.BasePlayListInteractor
import com.practicum.playlistmaker.basePlayList.domain.db.BasePlayListRepository
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

class BasePlayListInteractorImpl(
    private val basePlayListRepository: BasePlayListRepository,
) : BasePlayListInteractor {

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        basePlayListRepository.updateFavouritePlayList(playList)
    }

    override suspend fun addPlayListToFavouritePlayList(playList: PlayListCreateData) {
        basePlayListRepository.addPlayListToFavouritePlayList(playList)
    }

    override fun getPlayListById(playListId: Int): Flow<PlayListCreateData> {
        return basePlayListRepository.getPlayListById(playListId)
    }
}