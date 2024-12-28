package com.practicum.playlistmaker.mediaLibrary.ui.playList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PlayListViewModel(
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {

    private val playListSharedFlow = MutableSharedFlow<PlayListState>(replay = REPLAY_COUNT)
    fun getPlayListSharedFlow() = playListSharedFlow.asSharedFlow()

    init {
        loadPlayLists()
    }

    private fun loadPlayLists() {

        playListInteractor.getPlayList()
            .onEach { playLists ->
                if (playLists.isEmpty()) {
                    playListSharedFlow.emit(PlayListState.Empty)
                } else {
                    playListSharedFlow.emit(PlayListState.Content(playLists))
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        private const val REPLAY_COUNT = 1
    }

}