package com.practicum.playlistmaker.mediaLibrary.domain.db

import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun addTrackToPlayList(track: Track, playList: PlayListData)
    suspend fun removeTrackFromCurrentPlayList(track: Track, playList: PlayListData)
    suspend fun addPlayList(playList: PlayListData)
    suspend fun deletePlayList(playListId: Int)
    suspend fun updatePlayList(playList: PlayListData)
    fun getPlayList(): Flow<List<PlayListData>>
    fun getPlayListById(playListId: Int): Flow<PlayListData>
    suspend fun share(playList: PlayListData)
    fun getTracksForCurrentPlayListFlow(tracksId: List<Int>): Flow<List<Track>>

}