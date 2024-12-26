package com.practicum.playlistmaker.basePlayList.domain.db

import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

interface BasePlayListInteractor {

    suspend fun updateFavouritePlayList(playList: PlayListCreateData)
    suspend fun addPlayListToFavouritePlayList(playList: PlayListCreateData)
    fun getPlayListById(playListId: Int): Flow<PlayListCreateData>

}