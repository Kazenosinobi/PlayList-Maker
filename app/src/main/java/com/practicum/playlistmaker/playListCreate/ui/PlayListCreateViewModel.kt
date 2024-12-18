package com.practicum.playlistmaker.playListCreate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playListCreate.domain.db.PlayListCreateInteractor
import com.practicum.playlistmaker.playListCreate.domain.models.PlayListCreateData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PlayListCreateViewModel(
    private val playListCreateInteractor: PlayListCreateInteractor,
) : ViewModel() {

    private val playListCreateSharedFlow =
        MutableSharedFlow<PlayListCreateData>(replay = REPLAY_COUNT)

    fun getPlayListCreateSharedFlow() = playListCreateSharedFlow.asSharedFlow()

    fun updatePlayListData(image: String?, nameOfAlbum: String?, descriptionOfAlbum: String?) {
        viewModelScope.launch {
            playListCreateSharedFlow.emit(
                PlayListCreateData(
                    image,
                    nameOfAlbum,
                    descriptionOfAlbum
                )
            )
        }
    }

    fun savePlayList() {
        val data = playListCreateSharedFlow.replayCache.firstOrNull()
        if (data != null) {
            viewModelScope.launch {
                playListCreateInteractor.addPlayListToFavouritePlayList(data)
            }
        }
    }

    private companion object {
        private const val REPLAY_COUNT = 1
    }
}