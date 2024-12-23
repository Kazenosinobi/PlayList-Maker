package com.practicum.playlistmaker.playListScreen.data.impl

import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.playlistmaker.playListCreate.data.db.entity.mapToPlayListEntity
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.playListCreate.domain.models.mapToPlayListEntity
import com.practicum.playlistmaker.playListScreen.domain.db.PlayListScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListScreenRepositoryImpl(
    private val appDatabase: AppDatabase,
) : PlayListScreenRepository {

    override suspend fun deletePlayListAtFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().deletePlayList(playList.mapToPlayListEntity())
    }

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().updatePlayList(playList.mapToPlayListEntity())
    }

    override fun getPlayListById(playListId: Int): Flow<PlayListCreateData> =
        appDatabase.playListDao().getPlayListById(playListId)
            .map { playListEntity ->
                playListEntity.mapToPlayListEntity()
            }
}