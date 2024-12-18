package com.practicum.playlistmaker.playListCreate.domain.db

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

interface PlayListCreateRepository {

    suspend fun addPlayListToFavouritePlayList(playList: PlayListCreateData)
    suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData)
    suspend fun updateFavouritePlayList(playList: PlayListCreateData)
    fun getFavouritePlayList(): Flow<List<PlayListCreateData>>

}