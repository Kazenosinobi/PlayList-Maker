package com.practicum.playlistmaker.playListScreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playListScreen.domain.db.PlayListScreenInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PlayListScreenViewModel(
    private val playListId: Int,
    private val playListScreenInteractor: PlayListScreenInteractor
) : ViewModel() {

    private val playListScreenStateFlow =
        MutableStateFlow<PlayListScreenState>(PlayListScreenState.Empty)

    fun getPlayListScreenStateFlow() = playListScreenStateFlow.asStateFlow()

    init {
        loadPlayListById(playListId)
    }

    private fun loadPlayListById(playListId: Int) {

        playListScreenInteractor.getPlayListById(playListId)
            .onEach { playList ->
                playListScreenStateFlow.emit(PlayListScreenState.Content(playList))
            }
            .launchIn(viewModelScope)
    }
}