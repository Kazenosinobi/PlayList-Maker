package com.practicum.playlistmaker.mediaLibrary.domain.db

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData)
    suspend fun updateFavouritePlayList(playList: PlayListCreateData)
    fun getFavouritePlayList(): Flow<List<PlayListCreateData>>

}