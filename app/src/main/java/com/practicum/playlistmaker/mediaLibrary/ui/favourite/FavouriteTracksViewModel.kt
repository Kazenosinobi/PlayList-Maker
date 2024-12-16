package com.practicum.playlistmaker.mediaLibrary.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.db.FavouriteTracksInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FavouriteTracksViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
) : ViewModel() {

    private val favouriteSharedFlow = MutableSharedFlow<FavouriteState>(replay = REPLAY_COUNT)
    fun getFavouriteSharedFlow() = favouriteSharedFlow.asSharedFlow()

    init {
        loadFavouriteTracks()
    }

    private fun loadFavouriteTracks() {

        favouriteTracksInteractor.getFavouriteTracks()
            .onEach { tracks ->
                if (tracks.isEmpty()) {
                    favouriteSharedFlow.emit(FavouriteState.Empty)
                } else {
                    favouriteSharedFlow.emit(FavouriteState.Content(tracks))
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        private const val REPLAY_COUNT = 1
    }
}