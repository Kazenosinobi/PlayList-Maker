package com.practicum.playlistmaker.mediaLibrary.domain.impl

import com.practicum.playlistmaker.mediaLibrary.domain.db.FavouriteTracksInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.db.FavouriteTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val favouriteTracksRepository: FavouriteTracksRepository,
) : FavouriteTracksInteractor {

    override suspend fun addTrackToFavouriteTracks(track: Track) {
        favouriteTracksRepository.addTrackToFavouriteTracks(track)
    }

    override suspend fun deleteTrackAtFavouriteTracks(track: Track) {
        favouriteTracksRepository.deleteTrackAtFavouriteTracks(track)
    }

    override fun getFavouriteTracks(): Flow<List<Track>> =
        favouriteTracksRepository.getFavouriteTracks()

    override fun isFavourite(trackId: Int): Flow<Boolean> =
        favouriteTracksRepository.isFavourite(trackId)
}