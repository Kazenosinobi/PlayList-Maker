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

}