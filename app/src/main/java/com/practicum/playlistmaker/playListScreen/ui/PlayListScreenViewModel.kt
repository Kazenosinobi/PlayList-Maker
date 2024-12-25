package com.practicum.playlistmaker.playListScreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import com.practicum.playlistmaker.playListScreen.domain.api.ButtonsPlayListScreenInteractor
import com.practicum.playlistmaker.playListScreen.domain.db.PlayListScreenInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayListScreenViewModel(
    private val playListId: Int,
    private val playListScreenInteractor: PlayListScreenInteractor,
    private val buttonsPlayListScreenInteractor: ButtonsPlayListScreenInteractor
) : ViewModel() {

    var playList: PlayListCreateData? = null
        private set

    private val playListScreenStateFlow =
        MutableStateFlow<PlayListScreenState>(PlayListScreenState.Empty)

    fun getPlayListScreenStateFlow() = playListScreenStateFlow.asStateFlow()

    init {
        loadPlayListById(playListId)
    }

    fun removeTrackFromPlayList(track: Track) {
        viewModelScope.launch {
            playList?.removeTrack(track)
                ?.let {
                    playListScreenInteractor.updateFavouritePlayList(it)
                    playListScreenStateFlow.emit(PlayListScreenState.Content(it))
                }
        }
    }

    fun sharePlayList() {
        viewModelScope.launch {
            playList?.let { buttonsPlayListScreenInteractor.share(it) }
        }
    }

    private fun loadPlayListById(playListId: Int) {
        viewModelScope.launch {
            val currentPlayList = playListScreenInteractor.getPlayListById(playListId)
            playListScreenStateFlow.emit(PlayListScreenState.Content(currentPlayList))
            playList = currentPlayList
        }
    }
}