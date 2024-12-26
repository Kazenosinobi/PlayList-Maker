package com.practicum.playlistmaker.playListScreen.domain.db

import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

interface PlayListScreenInteractor {

    suspend fun updateFavouritePlayList(playList: PlayListCreateData)
    fun getPlayListById(playListId: Int): Flow<PlayListCreateData>

}