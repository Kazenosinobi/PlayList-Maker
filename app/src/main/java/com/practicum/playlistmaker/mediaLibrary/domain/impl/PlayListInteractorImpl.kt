package com.practicum.playlistmaker.mediaLibrary.domain.impl

import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListRepository
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val playListRepository: PlayListRepository,
) : PlayListInteractor {

    override suspend fun addPlayList(playList: PlayListData) {
        playListRepository.addPlayList(playList)
    }

    override suspend fun deletePlayList(playListId: Int) {
        playListRepository.deletePlayList(playListId)
    }

    override suspend fun updatePlayList(playList: PlayListData) {
        playListRepository.updatePlayList(playList)
    }

    override fun getPlayList(): Flow<List<PlayListData>> = playListRepository.getPlayList()


    override fun getPlayListById(playListId: Int): Flow<PlayListData> =
        playListRepository.getPlayListById(playListId)

    override fun share(playList: PlayListData) {
        playListRepository.share(playList)
    }

}