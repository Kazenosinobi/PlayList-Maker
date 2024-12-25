package com.practicum.playlistmaker.playListScreen.domain.db

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData

interface PlayListScreenRepository {

    suspend fun updateFavouritePlayList(playList: PlayListCreateData)
    suspend fun getPlayListById(playListId: Int): PlayListCreateData

}