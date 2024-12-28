package com.practicum.playlistmaker.playListScreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlayListScreenViewModel(
    private val playListId: Int,
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {

    var playList: PlayListData? = null
        private set

    private val playListScreenStateFlow =
        MutableStateFlow<PlayListScreenState>(PlayListScreenState.Empty)

    fun getPlayListScreenStateFlow() = playListScreenStateFlow.asStateFlow()

    init {
        loadPlayList()
    }

    fun removeTrackFromPlayList(track: Track) {
        viewModelScope.launch {
            playList?.let { playListInteractor.removeTrackFromCurrentPlayList(track, it) }
        }
    }

    fun sharePlayList() {
        viewModelScope.launch {
            playList?.let { playListInteractor.share(it) }
        }
    }

    private fun loadPlayList() {
        playListInteractor.getPlayListById(playListId)
            .onEach { playList ->
                this.playList = playList
                playListInteractor.getTracksForCurrentPlayListFlow(playList.tracksId)
                    .onEach { tracks ->
                        playListScreenStateFlow.emit(PlayListScreenState.Content(playList, tracks))
                    }
                    .launchIn(viewModelScope)
            }
            .launchIn(viewModelScope)


    }
}