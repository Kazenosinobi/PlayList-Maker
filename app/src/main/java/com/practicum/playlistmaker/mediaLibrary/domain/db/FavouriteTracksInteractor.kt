package com.practicum.playlistmaker.mediaLibrary.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {

    suspend fun addTrackToFavouriteTracks(track: Track)
    suspend fun deleteTrackAtFavouriteTracks(track: Track)
    fun getFavouriteTracks(): Flow<List<Track>>
    fun isFavourite(trackId: Int): Flow<Boolean>
}