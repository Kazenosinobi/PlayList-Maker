package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.mediaLibrary.domain.db.FavouriteTracksRepository
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
    private val favouriteTracksRepository: FavouriteTracksRepository
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
                    val favouriteTrackIds = favouriteTracksRepository.getFavouriteTrackIds()
                    val tracksWithFavouriteStatus = tracks.map {trackDto ->
                        val track = trackDto.mapToTrack()
                        track.copy(isFavorite = favouriteTrackIds.contains(track.trackId))
                    }
                    emit(ViewState.Success(tracksWithFavouriteStatus))
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

    override fun getSearchHistory(): Array<Track> {
        return localStorage.getSearchHistory()
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