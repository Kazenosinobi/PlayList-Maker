package com.practicum.playlistmaker.bottomSheet.playListBottomSheet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.mediaLibrary.ui.playList.PlayListState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlayListBottomSheetViewModel(
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {

    private val playListSharedFlow = MutableSharedFlow<PlayListState>(replay = REPLAY_COUNT)
    fun getPlayListSharedFlow() = playListSharedFlow.asSharedFlow()

    init {
        loadPlayLists()
    }

    fun addTrackToPlayList(track: Track, playList: PlayListData) {
        viewModelScope.launch {
            playListInteractor.updatePlayList(playList.addTrack(track))
            playListSharedFlow.emit(PlayListState.Content(listOf(playList.addTrack(track))))
        }
    }

    private fun loadPlayLists() {

        playListInteractor.getPlayList()
            .onEach { tracks ->
                if (tracks.isEmpty()) {
                    playListSharedFlow.emit(PlayListState.Empty)
                } else {
                    playListSharedFlow.emit(PlayListState.Content(tracks))
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        private const val REPLAY_COUNT = 1
    }

}