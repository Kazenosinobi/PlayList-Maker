package com.practicum.playlistmaker.mediaLibrary.domain.impl

import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListRepository
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val playListRepository: PlayListRepository,
) : PlayListInteractor {

    override suspend fun addTrackToPlayList(track: Track, playList: PlayListData) {
        playListRepository.addTrackToPlayList(track, playList)
    }

    override suspend fun removeTrackFromCurrentPlayList(track: Track, playList: PlayListData) {
        playListRepository.removeTrackFromCurrentPlayList(track, playList)
    }

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

    override suspend fun share(playList: PlayListData) {
        playListRepository.share(playList)
    }

    override fun getTracksForCurrentPlayListFlow(tracksId: List<Int>): Flow<List<Track>> =
        playListRepository.getTracksForCurrentPlayListFlow(tracksId)


}