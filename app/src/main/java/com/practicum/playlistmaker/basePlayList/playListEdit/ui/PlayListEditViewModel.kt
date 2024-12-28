package com.practicum.playlistmaker.basePlayList.playListEdit.ui

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.basePlayList.ui.BasePlayListViewModel
import com.practicum.playlistmaker.mediaLibrary.domain.db.PlayListInteractor
import com.practicum.playlistmaker.mediaLibrary.domain.models.PlayListData
import com.practicum.playlistmaker.playListScreen.ui.PlayListScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlayListEditViewModel(
    private val playListId: Int,
    private val playListInteractor: PlayListInteractor,
) : BasePlayListViewModel(playListInteractor) {

    private var playList: PlayListData? = null

    private val playListEditStateFlow =
        MutableStateFlow<PlayListScreenState>(PlayListScreenState.Empty)

    fun getPlayListEditStateFlow() = playListEditStateFlow.asStateFlow()

    init {
        loadPlayList()
    }

    fun updatePlayList(
        image: String?,
        nameOfAlbum: String?,
        descriptionOfAlbum: String?
    ) {
        viewModelScope.launch {
            editPlayList(
                image,
                nameOfAlbum,
                descriptionOfAlbum
            )?.let { playList ->
                playListInteractor.updatePlayList(playList)
            }
        }
    }

    private fun loadPlayList() {
        playListInteractor.getPlayListById(playListId)
            .onEach { playList ->
                playListEditStateFlow.emit(PlayListScreenState.Content(playList))
                this.playList = playList
            }
            .launchIn(viewModelScope)
    }

    private fun editPlayList(
        image: String?,
        nameOfAlbum: String?,
        descriptionOfAlbum: String?
    ): PlayListData? {
        Log.d("My log", "editPlayList: ${image.toString()}")
        Log.d("My log", "editPlayList: ${playList?.image.toString()}")
        Log.d("My log", "editPlayList: ${playList?.image?.equals(image)}")
        Log.d("My log", "editPlayList: ${playList?.image?.hashCode()}")
        Log.d("My log", "editPlayList: ${image?.hashCode()}")
        return playList?.copy(
            image = image,
            nameOfAlbum = nameOfAlbum,
            descriptionOfAlbum = descriptionOfAlbum
        )

    }
}