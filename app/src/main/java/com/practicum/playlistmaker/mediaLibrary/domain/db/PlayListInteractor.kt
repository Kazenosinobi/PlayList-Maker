package com.practicum.playlistmaker.mediaLibrary.domain.db

import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun updateFavouritePlayList(playList: PlayListCreateData)
    fun getFavouritePlayList(): Flow<List<PlayListCreateData>>

}