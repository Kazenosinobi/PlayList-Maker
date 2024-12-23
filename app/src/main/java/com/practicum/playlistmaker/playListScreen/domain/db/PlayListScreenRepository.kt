package com.practicum.playlistmaker.playListScreen.domain.db

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

interface PlayListScreenRepository {

    suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData)
    suspend fun updateFavouritePlayList(playList: PlayListCreateData)
    fun getPlayListById(playListId: Int): Flow<PlayListCreateData>

}