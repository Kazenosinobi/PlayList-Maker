package com.practicum.playlistmaker.mediaLibrary.domain.db

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    suspend fun updateFavouritePlayList(playList: PlayListCreateData)
    fun getFavouritePlayList(): Flow<List<PlayListCreateData>>

}