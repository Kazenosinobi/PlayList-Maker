package com.practicum.playlistmaker.mediaLibrary.domain.db

import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    suspend fun addPlayList(playList: PlayListData)
    suspend fun deletePlayList(playListId: Int)
    suspend fun updatePlayList(playList: PlayListData)
    fun getPlayList(): Flow<List<PlayListData>>
    fun getPlayListById(playListId: Int): Flow<PlayListData>
    fun share(playList: PlayListData)

}