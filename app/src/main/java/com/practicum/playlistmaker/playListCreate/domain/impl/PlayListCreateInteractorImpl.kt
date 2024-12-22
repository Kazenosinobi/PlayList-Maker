package com.practicum.playlistmaker.playListCreate.domain.impl

import com.practicum.playlistmaker.playListCreate.domain.db.PlayListCreateInteractor
import com.practicum.playlistmaker.playListCreate.domain.db.PlayListCreateRepository
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.Flow

class PlayListCreateInteractorImpl(
    private val playListCreateRepository: PlayListCreateRepository,
) : PlayListCreateInteractor {

    override suspend fun addPlayListToFavouritePlayList(playList: PlayListCreateData) {
        playListCreateRepository.addPlayListToFavouritePlayList(playList)
    }

}