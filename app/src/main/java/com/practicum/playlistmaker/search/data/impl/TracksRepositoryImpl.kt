package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.dto.mapToTrack
import com.practicum.playlistmaker.search.data.localStorage.SearchHistory
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: SearchHistory,
    private val appDatabase: AppDatabase,
) : TracksRepository {

    private val historyList = arrayListOf<Track>()

    init {
        loadHistory()
    }

    override fun searchTracks(expression: String): Flow<ViewState> = flow {
        networkClient.doRequest(expression)
            .onSuccess { tracks ->
                if (tracks.isEmpty()) {
                    emit(ViewState.EmptyError)
                } else {
                    val tracksIds = appDatabase.trackDao().getTrackIds()
                    val updatedTracks = tracks.map {
                        val isFavourite = tracksIds.contains(it.trackId)
                        it.copy(isFavorite = isFavourite)
                    }
                    emit(ViewState.Success(updatedTracks.map {
                        it.mapToTrack()
                    }))
                }
            }
            .onFailure { error ->
                when (error) {
                    is IOException -> {
                        emit(ViewState.NetworkError)
                    }

                    else -> {
                        emit(ViewState.EmptyError)
                    }
                }
            }
    }

    override suspend fun getSearchHistory(): Array<Track> {
        val historyTracks = localStorage.getSearchHistory()
        val trackIds = appDatabase.trackDao().getTrackIds()

        return historyTracks.map {track ->
            val isFavourite = trackIds.contains(track.trackId)
            track.copy(isFavorite = isFavourite)
        }.toTypedArray()
    }

    override fun saveSearchTrackHistory(tracks: Array<Track>) {
        localStorage.saveSearchTrackHistory(tracks)
    }

    override fun addToTrackHistory(track: Track) {
        val existingTrackIndex = historyList.indexOfFirst { it.trackId == track.trackId }
        if (existingTrackIndex != -1) {
            historyList.removeAt(existingTrackIndex)
        }
        if (historyList.size >= TRACKS_HISTORY_MAX_SIZE) {
            historyList.removeAt(historyList.lastIndex)
        }
        historyList.add(0, track)

        saveSearchTrackHistory(historyList.toTypedArray())
    }

    override fun clearHistory() {
        historyList.clear()
        saveSearchTrackHistory(emptyArray())
    }

    private fun loadHistory() {
        historyList.addAll(getSearchHistory())
    }

    private companion object {
        private const val TRACKS_HISTORY_MAX_SIZE = 10
    }
}