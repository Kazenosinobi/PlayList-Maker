package com.practicum.playlistmaker.playListCreate.domain.db

import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

interface PlayListCreateInteractor {

    suspend fun addPlayListToFavouritePlayList(playList: PlayListCreateData)

}