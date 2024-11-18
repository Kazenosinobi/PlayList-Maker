package com.practicum.playlistmaker.mediaLibrary.data.impl

import com.practicum.playlistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.playlistmaker.mediaLibrary.data.db.entity.mapToTrack
import com.practicum.playlistmaker.mediaLibrary.domain.db.FavouriteTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.models.mapToTrackEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
) : FavouriteTracksRepository {

    override suspend fun addTrackToFavouriteTracks(track: Track) {
        appDatabase.trackDao().addTrack(track.mapToTrackEntity())
    }

    override suspend fun deleteTrackAtFavouriteTracks(track: Track) {
        appDatabase.trackDao().deleteTrack(track.mapToTrackEntity())
    }

    override fun getFavouriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(tracks.map { it.mapToTrack() })
    }

    override suspend fun getFavouriteTrackIds(): List<Int> =
        appDatabase.trackDao().getFavouriteTrackIds()
}