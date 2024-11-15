package com.practicum.playlistmaker.mediaLibrary.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.db.FavouriteTracksInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class FavouriteTracksViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
) : ViewModel() {

    private val favouriteSharedFlow = MutableSharedFlow<FavouriteState>()
    fun getFavouriteSharedFlow() = favouriteSharedFlow.asSharedFlow()

    init {
        loadFavouriteTracks()
    }

    private fun loadFavouriteTracks() {
        viewModelScope.launch {
            favouriteTracksInteractor.getFavouriteTracks().collect { tracks ->
                if (tracks.isEmpty()) {
                    favouriteSharedFlow.emit(FavouriteState.Empty)
                } else {
                    favouriteSharedFlow.emit(FavouriteState.Content(tracks))
                }
            }
        }
    }
}