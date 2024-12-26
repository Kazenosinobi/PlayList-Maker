package com.practicum.playlistmaker.mediaLibrary.data.impl

import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListRepository
import com.practicum.playlistmaker.basePlayList.data.db.entity.mapToPlayListCreateData
import com.practicum.playlistmaker.basePlayList.domain.models.PlayListCreateData
import com.practicum.playlistmaker.basePlayList.domain.models.mapToPlayListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
) : PlayListRepository {

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().updatePlayList(playList.mapToPlayListEntity())
    }

    override fun getFavouritePlayList(): Flow<List<PlayListCreateData>> =
        appDatabase.playListDao().getPlayList()
            .map { playListsEntity ->
                playListsEntity.map { playListEntity ->
                    playListEntity.mapToPlayListCreateData()
                }
            }

}