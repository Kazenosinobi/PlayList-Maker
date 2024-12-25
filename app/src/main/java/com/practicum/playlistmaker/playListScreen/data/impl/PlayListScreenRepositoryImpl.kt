package com.practicum.playlistmaker.playListScreen.data.impl

import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.playlistmaker.playListCreate.data.db.entity.mapToPlayListEntity
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.playListCreate.domain.models.mapToPlayListEntity
import com.practicum.playlistmaker.playListScreen.domain.db.PlayListScreenRepository

class PlayListScreenRepositoryImpl(
    private val appDatabase: AppDatabase,
) : PlayListScreenRepository {

    override suspend fun updateFavouritePlayList(playList: PlayListCreateData) {
        appDatabase.playListDao().updatePlayList(playList.mapToPlayListEntity())
    }

    override suspend fun getPlayListById(playListId: Int): PlayListCreateData =
        appDatabase.playListDao().getPlayListById(playListId).mapToPlayListEntity()

}