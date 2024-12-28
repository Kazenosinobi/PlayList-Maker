package com.practicum.playlistmaker.bottomSheet.playListBottomSheet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.mediaLibrary.ui.playList.PlayListState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlayListBottomSheetViewModel(
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {

    private val playListStateFlow = MutableStateFlow<PlayListState>(PlayListState.Empty)
    fun getPlayListStateFlow() = playListStateFlow.asStateFlow()

    init {
        loadPlayLists()
    }

    fun addTrackToPlayList(track: Track, playList: PlayListData) {
        viewModelScope.launch {
            playListInteractor.addTrackToPlayList(track, playList)
        }
    }

    private fun loadPlayLists() {

        playListInteractor.getPlayList()
            .onEach { playLists ->
                if (playLists.isEmpty()) {
                    playListStateFlow.emit(PlayListState.Empty)
                } else {
                    playListStateFlow.emit(PlayListState.Content(playLists))
                }
            }
            .launchIn(viewModelScope)
    }

}