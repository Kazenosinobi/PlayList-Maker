package com.practicum.playlistmaker.basePlayList.data.impl

import com.practicum.playlistmaker.basePlayList.data.db.entity.mapToPlayListCreateData
import com.practicum.playlistmaker.basePlayList.domain.db.BasePlayListRepository
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import com.practicum.playlistmaker.basePlayList.domain.models.mapToPlayListEntity
import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BasePlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
) : BasePlayListRepository {

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().updatePlayList(playList.mapToPlayListEntity())
    }

    override suspend fun addPlayListToFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().addPlayList(playList.mapToPlayListEntity())
    }

    override fun getPlayListById(playListId: Int): Flow<PlayListCreateData> =
        appDatabase.playListDao().getPlayListById(playListId)
            .map { playList ->
                playList.mapToPlayListCreateData()
            }

}