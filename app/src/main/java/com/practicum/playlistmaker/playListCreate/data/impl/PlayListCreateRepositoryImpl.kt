package com.practicum.playlistmaker.playListCreate.data.impl

import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.playlistmaker.playListCreate.data.db.entity.mapToPlayListEntity
import com.practicum.playlistmaker.playListCreate.domain.db.PlayListCreateRepository
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.playListCreate.domain.models.mapToPlayListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListCreateRepositoryImpl(
    private val appDatabase: AppDatabase,
) : PlayListCreateRepository {

    override suspend fun addPlayListToFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().addPlayList(playList.mapToPlayListEntity())
    }

    override suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().deletePlayList(playList.mapToPlayListEntity())
    }

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().updatePlayList(playList.mapToPlayListEntity())
    }

    override fun getFavouritePlayList(): Flow<List<PlayListCreateData>> =
        appDatabase.playListDao().getPlayList()
            .map { playListsEntity ->
                playListsEntity.map { playListEntity ->
                    playListEntity.mapToPlayListEntity()
                }
            }

}